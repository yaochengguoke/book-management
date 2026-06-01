// 智慧图书管理系统 - 微信小程序
App({
  globalData: {
    baseUrl: 'https://springboot-pzz8-264849-5-1439013668.sh.run.tcloudbase.com', // 开发环境，上线改为HTTPS域名
    token: '',
    userInfo: null
  },

  onLaunch() {
    // 检查本地存储的token
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
    }
  },

  // 检查登录状态
  checkLogin() {
    if (!this.globalData.token) {
      wx.navigateTo({ url: '/pages/mine/mine' });
      return false;
    }
    return true;
  },

  // 微信登录
  wxLogin(callback) {
    wx.login({
      success: (res) => {
        if (res.code) {
          wx.request({
            url: `${this.globalData.baseUrl}/api/auth/wx-login`,
            method: 'POST',
            data: { code: res.code },
            success: (resp) => {
              if (resp.data.code === 200) {
                const token = resp.data.data.token;
                this.globalData.token = token;
                wx.setStorageSync('token', token);
                this.globalData.userInfo = resp.data.data;
                callback && callback(true);
              } else {
                callback && callback(false);
              }
            },
            fail: () => callback && callback(false)
          });
        }
      }
    });
  }
});
