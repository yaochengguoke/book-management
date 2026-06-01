const { userApi } = require('../../utils/api');

Page({
  data: {
    users: [],
    roles: ['USER', 'ADMIN'],
    roleIndex: 0,
    form: { username: '', password: '', role: 'USER' }
  },

  onShow() { this.loadUsers(); },

  async loadUsers() {
    try {
      const data = await userApi.list();
      this.setData({ users: data.content || [] });
    } catch (e) { /* ignore */ }
  },

  onField(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({ [`form.${field}`]: e.detail.value });
  },

  onRoleChange(e) {
    this.setData({ roleIndex: e.detail.value, 'form.role': this.data.roles[e.detail.value] });
  },

  async addUser() {
    const { form } = this.data;
    if (!form.username || !form.password) {
      return wx.showToast({ title: '用户名和密码必填', icon: 'none' });
    }
    try {
      await userApi.add({ username: form.username, password: form.password, role: form.role });
      wx.showToast({ title: '添加成功', icon: 'success' });
      this.setData({ form: { username: '', password: '', role: 'USER' }, roleIndex: 0 });
      this.loadUsers();
    } catch (e) { /* error handled */ }
  },

  async deleteUser(e) {
    const res = await new Promise(r => wx.showModal({ title: '确认删除', content: '确定删除该用户吗？', success: r }));
    if (!res.confirm) return;
    try {
      await userApi.delete(e.currentTarget.dataset.id);
      wx.showToast({ title: '删除成功', icon: 'success' });
      this.loadUsers();
    } catch (e) { /* error handled */ }
  }
});
