const { borrowApi } = require('../../utils/api');

Page({
  data: { borrows: [] },

  onShow() { this.loadBorrows(); },

  async loadBorrows() {
    try {
      const data = await borrowApi.list();
      this.setData({ borrows: (data.content || data || []).slice(0, 50) });
    } catch (e) {
      this.setData({ borrows: [] });
    }
  },

  async returnBook(e) {
    const res = await new Promise(r => wx.showModal({ title: '确认归还', content: '确定归还该书吗？', success: r }));
    if (!res.confirm) return;
    try {
      await borrowApi.return(e.currentTarget.dataset.id);
      wx.showToast({ title: '归还成功', icon: 'success' });
      this.loadBorrows();
    } catch (e) { /* error handled */ }
  }
});
