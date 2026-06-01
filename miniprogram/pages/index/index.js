const { bookApi, borrowApi, userApi } = require('../../utils/api');

Page({
  data: {
    stats: { bookCount: 0, borrowCount: 0, userCount: 0, totalBorrows: 0 },
    categories: [], topBooks: [], recentBooks: []
  },
  onShow() { this.loadAll(); },
  async loadAll() {
    try {
      const [books, users, stats, cats] = await Promise.all([
        bookApi.list({ size: 1 }), userApi.list(),
        bookApi.top().catch(() => ({ totalBooks: 0, activeBorrows: 0, totalBorrows: 0, topBooks: [] })),
        bookApi.categories().catch(() => [])
      ]);
      this.setData({
        stats: {
          bookCount: books.totalElements || 0, userCount: users.totalElements || 0,
          borrowCount: stats.activeBorrows || 0, totalBorrows: stats.totalBorrows || 0
        },
        categories: cats || [], topBooks: (stats.topBooks || []).slice(0, 5),
        recentBooks: (books.content || []).slice(0, 10)
      });
    } catch (e) {}
  },
  goCategory(e) { wx.navigateTo({ url: `/pages/books/books?category=${e.currentTarget.dataset.cat}` }); },
  goBooks() { wx.switchTab({ url: '/pages/books/books' }); },
  goDetail(e) { wx.navigateTo({ url: `/pages/book-detail/book-detail?id=${e.currentTarget.dataset.id}` }); }
});
