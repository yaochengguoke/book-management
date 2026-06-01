const { bookApi, borrowApi, userApi } = require('../../utils/api');

Page({
  data: {
    stats: { bookCount: 0, borrowCount: 0, userCount: 0, totalBorrows: 0 },
    recentBooks: []
  },

  onShow() {
    this.loadStats();
    this.loadRecentBooks();
  },

  async loadStats() {
    try {
      const [books, users, borrowStats] = await Promise.all([
        bookApi.list({ size: 1 }),
        userApi.list(),
        borrowApi.statistics().catch(() => ({ totalBorrows: 0, activeBorrows: 0 }))
      ]);
      this.setData({
        stats: {
          bookCount: books.totalElements || 0,
          userCount: users.totalElements || 0,
          borrowCount: borrowStats.activeBorrows || 0,
          totalBorrows: borrowStats.totalBorrows || 0
        }
      });
    } catch (e) {
      console.error('加载统计失败', e);
    }
  },

  async loadRecentBooks() {
    try {
      const books = await bookApi.list({ size: 5, sortBy: 'id', sortDir: 'desc' });
      this.setData({ recentBooks: books.content || [] });
    } catch (e) {
      console.error('加载图书失败', e);
    }
  },

  goBooks() { wx.navigateTo({ url: '/pages/books/books' }); },
  goBorrows() { wx.navigateTo({ url: '/pages/borrows/borrows' }); },
  goPoints() { wx.navigateTo({ url: '/pages/points/points' }); },
  goMine() { wx.navigateTo({ url: '/pages/mine/mine' }); },
  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/book-detail/book-detail?id=${id}` });
  }
});
