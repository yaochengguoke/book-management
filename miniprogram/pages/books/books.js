const { bookApi } = require('../../utils/api');
const app = getApp();

Page({
  data: {
    books: [],
    keyword: '',
    page: 0,
    hasMore: true,
    loading: false,
    isAdmin: false
  },

  onShow() {
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({ isAdmin: userInfo && userInfo.role === 'ADMIN' });
    this.setData({ books: [], page: 0, hasMore: true });
    this.loadBooks();
  },

  onReachBottom() {
    if (this.data.hasMore) this.loadBooks();
  },

  async loadBooks() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    try {
      const params = { page: this.data.page, size: 10 };
      const res = this.data.keyword
        ? await bookApi.search(this.data.keyword)
        : await bookApi.list(params);
      const newBooks = res.content || [];
      this.setData({
        books: this.data.page === 0 ? newBooks : [...this.data.books, ...newBooks],
        page: this.data.page + 1,
        hasMore: newBooks.length === 10
      });
    } catch (e) { /* error handled in api.js */ }
    this.setData({ loading: false });
  },

  onSearch(e) { this.setData({ keyword: e.detail.value }); },

  doSearch() {
    this.setData({ books: [], page: 0, hasMore: true });
    this.loadBooks();
  },

  goDetail(e) {
    wx.navigateTo({ url: `/pages/book-detail/book-detail?id=${e.currentTarget.dataset.id}` });
  },

  showAdd() {
    wx.navigateTo({ url: '/pages/book-detail/book-detail?mode=add' });
  },

  async deleteBook(e) {
    const id = e.currentTarget.dataset.id;
    const res = await new Promise(r => wx.showModal({ title: '确认删除', content: '确定删除该图书吗？', success: r }));
    if (!res.confirm) return;
    try {
      await bookApi.delete(id);
      wx.showToast({ title: '删除成功', icon: 'success' });
      this.setData({ books: [], page: 0, hasMore: true });
      this.loadBooks();
    } catch (e) { /* error handled */ }
  }
});
