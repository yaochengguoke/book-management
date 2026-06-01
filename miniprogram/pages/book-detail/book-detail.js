const { bookApi, borrowApi } = require('../../utils/api');

Page({
  data: {
    book: {},
    mode: 'view',
    form: { title: '', author: '', isbn: '', publisher: '', price: '', quantity: '', description: '' }
  },

  onLoad(options) {
    if (options.mode === 'add') {
      this.setData({ mode: 'add' });
      wx.setNavigationBarTitle({ title: '添加图书' });
    } else if (options.id) {
      this.loadBook(options.id);
    }
  },

  async loadBook(id) {
    try {
      const book = await bookApi.detail(id);
      this.setData({ book });
    } catch (e) { /* error handled */ }
  },

  onField(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({ [`form.${field}`]: e.detail.value });
  },

  async submitAdd() {
    const { form } = this.data;
    if (!form.title || !form.author) {
      return wx.showToast({ title: '书名和作者必填', icon: 'none' });
    }
    try {
      const data = {
        ...form,
        price: parseFloat(form.price) || 0,
        quantity: parseInt(form.quantity) || 1
      };
      await bookApi.add(data);
      wx.showToast({ title: '添加成功', icon: 'success' });
      setTimeout(() => wx.navigateBack(), 1500);
    } catch (e) { /* error handled */ }
  },

  async borrowBook() {
    try {
      await borrowApi.create({ bookId: this.data.book.id, userId: 1 });
      wx.showToast({ title: '借阅成功', icon: 'success' });
      this.loadBook(this.data.book.id);
    } catch (e) { /* error handled */ }
  }
});
