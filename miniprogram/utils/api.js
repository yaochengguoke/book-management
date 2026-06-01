// API请求封装
const app = getApp();

const request = (url, method = 'GET', data = {}) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${app.globalData.baseUrl}${url}`,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': app.globalData.token ? `Bearer ${app.globalData.token}` : ''
      },
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data.data);
        } else if (res.data.code === 401) {
          wx.removeStorageSync('token');
          app.globalData.token = '';
          wx.showToast({ title: '请先登录', icon: 'none' });
          reject(res.data);
        } else {
          wx.showToast({ title: res.data.message || '请求失败', icon: 'none' });
          reject(res.data);
        }
      },
      fail: (err) => {
        wx.showToast({ title: '网络错误', icon: 'none' });
        reject(err);
      }
    });
  });
};

// 图书相关API
const bookApi = {
  list: (params = {}) => request(`/api/books?page=${params.page || 0}&size=${params.size || 10}&sortBy=${params.sortBy || 'id'}&sortDir=${params.sortDir || 'desc'}`),
  search: (keyword) => request(`/api/books/search?title=${keyword}`),
  detail: (id) => request(`/api/books/${id}`),
  add: (data) => request('/api/books', 'POST', data),
  update: (id, data) => request(`/api/books/${id}`, 'PUT', data),
  delete: (id) => request(`/api/books/${id}`, 'DELETE')
};

// 借阅相关API
const borrowApi = {
  list: () => request('/api/borrows'),
  create: (data) => request('/api/borrows', 'POST', data),
  return: (id) => request(`/api/borrows/${id}/return`, 'PUT'),
  statistics: () => request('/api/borrows/statistics')
};

// 用户相关API
const userApi = {
  list: () => request('/api/users'),
  add: (data) => request('/api/users', 'POST', data),
  delete: (id) => request(`/api/users/${id}`, 'DELETE')
};

// 积分相关API
const pointsApi = {
  get: (userId) => request(`/api/points/user/${userId}`),
  signIn: (userId) => request(`/api/points/user/${userId}/signin`, 'POST'),
  earn: (userId, amount, desc) => request(`/api/points/user/${userId}/earn`, 'POST', { amount, description: desc })
};

// 认证API
const authApi = {
  login: (username, password) => request('/api/auth/login', 'POST', { username, password }),
  wxLogin: (code) => request('/api/auth/wx-login', 'POST', { code })
};

module.exports = { bookApi, borrowApi, userApi, pointsApi, authApi, request };
