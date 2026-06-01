const { pointsApi, reservationApi } = require('../../utils/api');

Page({
  data: { points: { balance: 0, totalEarned: 0, totalSpent: 0 }, reservations: [] },
  onShow() { this.loadAll(); },
  async loadAll() {
    try { this.setData({ points: await pointsApi.get(1) || {} }); } catch (e) {}
    try { this.setData({ reservations: await reservationApi.byUser(1) || [] }); } catch (e) {}
  },
  async doSignIn() {
    await pointsApi.signIn(1);
    wx.showToast({ title: '签到成功! +10积分', icon: 'success' });
    this.loadAll();
  },
  async cancelReservation(e) {
    const r = await new Promise(r => wx.showModal({ title: '取消预约', content: '确定取消吗？', success: r }));
    if (!r.confirm) return;
    await reservationApi.cancel(e.currentTarget.dataset.id);
    wx.showToast({ title: '已取消', icon: 'success' });
    this.loadAll();
  }
});
