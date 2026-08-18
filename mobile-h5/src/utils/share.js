export const showSharePanel = ({ title, description, url }) => {
  if (navigator.share) {
    navigator.share({
      title: title || '',
      text: description || '',
      url: url || window.location.href
    }).catch(() => {})
  } else {
    const text = `${title}\n${description}\n${url || window.location.href}`
    if (navigator.clipboard) {
      navigator.clipboard.writeText(text).then(() => {
        import('vant').then(({ showToast }) => {
          showToast('链接已复制到剪贴板')
        })
      }).catch(() => {
        import('vant').then(({ showToast }) => {
          showToast('复制失败，请手动复制')
        })
      })
    } else {
      import('vant').then(({ showDialog }) => {
        showDialog({
          title: '分享',
          message: text,
          confirmButtonText: '知道了'
        })
      })
    }
  }
}

export default {
  showSharePanel
}
