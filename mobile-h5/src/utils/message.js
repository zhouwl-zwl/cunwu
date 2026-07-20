import { showToast, showDialog, showNotify } from 'vant'

const message = {
  success(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showToast({
      message: opts.message,
      type: 'success',
      duration: opts.duration || 2000,
      position: opts.position || 'top'
    })
  },

  error(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showToast({
      message: opts.message,
      type: 'fail',
      duration: opts.duration || 2000,
      position: opts.position || 'top'
    })
  },

  warning(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showToast({
      message: opts.message,
      type: 'warning',
      duration: opts.duration || 2000,
      position: opts.position || 'top'
    })
  },

  info(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showToast({
      message: opts.message,
      type: 'info',
      duration: opts.duration || 2000,
      position: opts.position || 'top'
    })
  },

  loading(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showToast({
      message: opts.message || '加载中...',
      type: 'loading',
      duration: opts.duration || 0,
      forbidClick: opts.forbidClick !== false
    })
  },

  hideLoading() {
    showToast.clear()
  },

  confirm(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    return new Promise((resolve) => {
      showDialog({
        title: opts.title || '提示',
        message: opts.message,
        confirmButtonText: opts.confirmText || '确定',
        cancelButtonText: opts.cancelText || '取消',
        confirmButtonColor: opts.confirmColor || '#D22630',
        cancelButtonColor: opts.cancelColor || '#999'
      }).then(() => {
        resolve(true)
      }).catch(() => {
        resolve(false)
      })
    })
  },

  alert(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    return new Promise((resolve) => {
      showDialog({
        title: opts.title || '提示',
        message: opts.message,
        confirmButtonText: opts.confirmText || '确定',
        showCancelButton: false,
        confirmButtonColor: opts.confirmColor || '#D22630'
      }).then(() => {
        resolve(true)
      }).catch(() => {
        resolve(false)
      })
    })
  },

  notify(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showNotify({
      type: opts.type || 'primary',
      message: opts.message,
      duration: opts.duration || 3000,
      position: opts.position || 'top'
    })
  },

  toast(options) {
    const opts = typeof options === 'string' ? { message: options } : options
    showToast({
      message: opts.message,
      type: opts.type || 'text',
      duration: opts.duration || 2000,
      position: opts.position || 'top',
      forbidClick: opts.forbidClick || false
    })
  }
}

export default message