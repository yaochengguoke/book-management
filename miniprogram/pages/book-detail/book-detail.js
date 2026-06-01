const { bookApi, borrowApi, reviewApi, reservationApi } = require('../../utils/api');

Page({
  data: {
    book: {}, reviews: [], mode: 'view', showReview: false, reviewRating: 5, reviewContent: '',
    form: { title:'', author:'', isbn:'', publisher:'', category:'', coverImage:'', price:'', quantity:'', description:'' }
  },
  onLoad(options) {
    if (options.mode === 'add') this.setData({ mode: 'add' });
    else if (options.id) { this.loadBook(options.id); this.loadReviews(options.id); }
  },
  async loadBook(id) {
    try { this.setData({ book: await bookApi.detail(id) }); } catch (e) {}
  },
  async loadReviews(bookId) {
    try { this.setData({ reviews: await reviewApi.byBook(bookId) }); } catch (e) {}
  },

  // Add book
  onField(e) { this.setData({ [`form.${e.currentTarget.dataset.field}`]: e.detail.value }); },
  async submitAdd() {
    const f = this.data.form;
    if (!f.title || !f.author) return wx.showToast({ title: '书名和作者必填', icon: 'none' });
    await bookApi.add({...f, price: parseFloat(f.price)||0, quantity: parseInt(f.quantity)||1});
    wx.showToast({ title: '添加成功', icon: 'success' });
    setTimeout(() => wx.navigateBack(), 1500);
  },

  // Scan ISBN
  scanISBN() {
    wx.scanCode({ onlyFromCamera: true, scanType: ['barCode'], success: (res) => {
      this.setData({ 'form.isbn': res.result });
      // Try to fetch book info from douban/similar API
      wx.showToast({ title: 'ISBN已填入: ' + res.result, icon: 'none' });
    }});
  },

  // Borrow
  async borrowBook() {
    try {
      await borrowApi.create({ bookId: this.data.book.id, userId: 1 });
      wx.showToast({ title: '借阅成功!', icon: 'success' });
      this.loadBook(this.data.book.id);
    } catch (e) {}
  },

  // Reserve
  async reserveBook() {
    try {
      const r = await reservationApi.create({ bookId: this.data.book.id, userId: 1 });
      wx.showToast({ title: '预约成功! 排队第' + (r.queuePosition||'?') + '位', icon: 'success' });
    } catch (e) {}
  },

  // Review
  showReviewForm() { this.setData({ showReview: true }); },
  hideReviewForm() { this.setData({ showReview: false }); },
  setRating(e) { this.setData({ reviewRating: parseInt(e.currentTarget.dataset.r) }); },
  onReviewContent(e) { this.setData({ reviewContent: e.detail.value }); },
  async submitReview() {
    await reviewApi.create({ bookId: this.data.book.id, userId: 1, rating: this.data.reviewRating, content: this.data.reviewContent });
    wx.showToast({ title: '评价成功!', icon: 'success' });
    this.setData({ showReview: false, reviewContent: '', reviewRating: 5 });
    this.loadReviews(this.data.book.id);
    this.loadBook(this.data.book.id);
  }
});
