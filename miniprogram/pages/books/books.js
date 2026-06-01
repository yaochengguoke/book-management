const { bookApi } = require('../../utils/api');

Page({
  data: {
    books: [], categories: [], activeCat: '', keyword: '', page: 0, hasMore: true, loading: false, isAdmin: false
  },
  onLoad(options) {
    if (options.category) this.setData({ activeCat: options.category });
  },
  onShow() {
    const ui = wx.getStorageSync('userInfo');
    this.setData({ isAdmin: ui && ui.role === 'ADMIN' });
    this.setData({ books: [], page: 0, hasMore: true });
    this.loadBooks();
    bookApi.categories().then(cats => this.setData({ categories: cats || [] })).catch(() => {});
  },
  onReachBottom() { if (this.data.hasMore) this.loadBooks(); },

  async loadBooks() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    try {
      let res;
      if (this.data.keyword) res = await bookApi.search(this.data.keyword);
      else if (this.data.activeCat) res = await bookApi.byCategory(this.data.activeCat, this.data.page);
      else res = await bookApi.list({ page: this.data.page, size: 20 });
      const newBooks = res.content || [];
      this.setData({
        books: this.data.page === 0 ? newBooks : [...this.data.books, ...newBooks],
        page: this.data.page + 1,
        hasMore: newBooks.length === 20
      });
    } catch (e) {}
    this.setData({ loading: false });
  },

  onSearch(e) { this.setData({ keyword: e.detail.value }); },
  doSearch() { this.setData({ books: [], page: 0, hasMore: true, activeCat: '' }); this.loadBooks(); },
  filterCategory(e) {
    const cat = e.currentTarget.dataset.cat;
    this.setData({ activeCat: cat, keyword: '', books: [], page: 0, hasMore: true });
    this.loadBooks();
  },
  goDetail(e) { wx.navigateTo({ url: `/pages/book-detail/book-detail?id=${e.currentTarget.dataset.id}` }); },
  goAdd() { wx.navigateTo({ url: '/pages/book-detail/book-detail?mode=add' }); }
});
