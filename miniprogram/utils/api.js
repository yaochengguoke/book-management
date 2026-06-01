const app = getApp();

const request = (url, method = 'GET', data = {}) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${app.globalData.baseUrl}${url}`,
      method, data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': app.globalData.token ? `Bearer ${app.globalData.token}` : ''
      },
      success: (res) => {
        if (res.data.code === 200) resolve(res.data.data);
        else if (res.data.code === 401) {
          wx.removeStorageSync('token');
          app.globalData.token = '';
          wx.showToast({ title: '请先登录', icon: 'none' });
          reject(res.data);
        } else {
          wx.showToast({ title: res.data.message || '请求失败', icon: 'none' });
          reject(res.data);
        }
      },
      fail: (err) => { wx.showToast({ title: '网络错误', icon: 'none' }); reject(err); }
    });
  });
};

// 图书
const bookApi = {
  list: (params = {}) => request(`/api/books?page=${params.page || 0}&size=${params.size || 10}&sortBy=${params.sortBy || 'id'}&sortDir=${params.sortDir || 'desc'}`),
  search: (keyword) => request(`/api/books/search?title=${encodeURIComponent(keyword || '')}`),
  byCategory: (cat, page = 0) => request(`/api/books/search?category=${encodeURIComponent(cat)}&page=${page}&size=20`),
  categories: () => request('/api/books/categories'),
  detail: (id) => request(`/api/books/${id}`),
  add: (data) => request('/api/books', 'POST', data),
  update: (id, data) => request(`/api/books/${id}`, 'PUT', data),
  delete: (id) => request(`/api/books/${id}`, 'DELETE'),
  top: () => request('/api/export/stats')
};

// 借阅
const borrowApi = {
  list: () => request('/api/borrows'),
  create: (data) => request('/api/borrows', 'POST', data),
  return: (id) => request(`/api/borrows/${id}/return`, 'PUT')
};

// 用户
const userApi = {
  list: () => request('/api/users'),
  detail: (id) => request(`/api/users/${id}`),
  add: (data) => request('/api/users', 'POST', data),
  delete: (id) => request(`/api/users/${id}`, 'DELETE')
};

// 积分
const pointsApi = {
  get: (uid) => request(`/api/points/user/${uid}`),
  signIn: (uid) => request(`/api/points/user/${uid}/signin`, 'POST'),
  earn: (uid, amount, desc) => request(`/api/points/user/${uid}/earn`, 'POST', { amount, description: desc })
};

// 评价
const reviewApi = {
  byBook: (bookId) => request(`/api/reviews/book/${bookId}`),
  create: (data) => request('/api/reviews', 'POST', data)
};

// 预约
const reservationApi = {
  byUser: (uid) => request(`/api/reservations/user/${uid}`),
  create: (data) => request('/api/reservations', 'POST', data),
  cancel: (id) => request(`/api/reservations/${id}`, 'DELETE')
};

// 导出
const exportApi = {
  borrows: () => `${app.globalData.baseUrl}/api/export/borrows`
};

// 认证
const authApi = {
  login: (username, password) => request('/api/auth/login', 'POST', { username, password }),
  wxLogin: (code) => request('/api/auth/wx-login', 'POST', { code })
};

module.exports = { bookApi, borrowApi, userApi, pointsApi, reviewApi, reservationApi, exportApi, authApi, request };
