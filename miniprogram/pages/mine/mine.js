const { authApi } = require('../../utils/api');
const app = getApp();

Page({
  data: {
    userInfo: { username: '未登录', role: 'USER' },
    showLogin: false,
    username: '',
    password: ''
  },

  onShow() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) this.setData({ userInfo });
  },

  // 微信一键登录
  wxLogin() {
    app.wxLogin((success) => {
      if (success) {
        wx.showToast({ title: '登录成功', icon: 'success' });
        const userInfo = { username: '微信用户', role: 'USER' };
        wx.setStorageSync('userInfo', userInfo);
        this.setData({ userInfo });
      } else {
        wx.showToast({ title: '登录失败', icon: 'none' });
      }
    });
  },

  // 密码登录
  loginWithPassword() {
    this.setData({ showLogin: true });
  },

  onUsernameInput(e) { this.setData({ username: e.detail.value }); },
  onPasswordInput(e) { this.setData({ password: e.detail.value }); },

  async doLogin() {
    const { username, password } = this.data;
    if (!username || !password) {
      return wx.showToast({ title: '请输入账号和密码', icon: 'none' });
    }
    try {
      const data = await authApi.login(username, password);
      wx.setStorageSync('token', data.token);
      wx.setStorageSync('userInfo', { username: data.username, role: data.role });
      app.globalData.token = data.token;
      this.setData({ userInfo: { username: data.username, role: data.role }, showLogin: false });
      wx.showToast({ title: '登录成功', icon: 'success' });
    } catch (e) { /* error handled */ }
  },

  goUsers() {
    wx.navigateTo({ url: '/pages/users/users' });
  }
});
