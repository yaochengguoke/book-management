const { pointsApi } = require('../../utils/api');
const app = getApp();

Page({
  data: { points: { balance: 0, totalEarned: 0, totalSpent: 0 } },

  onShow() { this.loadPoints(); },

  async loadPoints() {
    try {
      const data = await pointsApi.get(1);
      this.setData({ points: data || {} });
    } catch (e) { /* ignore */ }
  },

  async doSignIn() {
    try {
      await pointsApi.signIn(1);
      wx.showToast({ title: '签到成功！+10积分', icon: 'success' });
      this.loadPoints();
    } catch (e) {
      wx.showToast({ title: '签到失败，请稍后再试', icon: 'none' });
    }
  }
});
