const { borrowApi, exportApi } = require('../../utils/api');

Page({
  data: { borrows: [] },
  onShow() { this.loadBorrows(); },

  async loadBorrows() {
    try {
      const data = await borrowApi.list();
      const now = new Date();
      const items = (data.content || data || []).map(b => ({
        ...b,
        overdue: b.status === 'BORROWED' && b.dueDate && new Date(b.dueDate) < now
      }));
      this.setData({ borrows: items });
    } catch (e) { this.setData({ borrows: [] }); }
  },

  async returnBook(e) {
    const r = await new Promise(r => wx.showModal({ title: '确认归还', content: '确定归还吗？', success: r }));
    if (!r.confirm) return;
    await borrowApi.return(e.currentTarget.dataset.id);
    wx.showToast({ title: '归还成功', icon: 'success' });
    this.loadBorrows();
  },

  exportData() {
    wx.setClipboardData({ data: exportApi.borrows });
    wx.showToast({ title: '链接已复制，浏览器打开下载', icon: 'none' });
  }
});
