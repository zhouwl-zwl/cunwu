import QRCode from 'qrcode.vue'
import { showToast } from 'vant'

export const shareConfig = {
  title: '罗卜田乡智慧村务平台',
  defaultUrl: window.location.href
}

export const showSharePanel = async (options = {}) => {
  const { title, text, url } = {
    title: options.title || shareConfig.title,
    text: options.text || '',
    url: options.url || window.location.href
  }

  if (navigator.share) {
    try {
      await navigator.share({ title, text, url })
      return true
    } catch (err) {
      if (err.name !== 'AbortError') {
        console.warn('Web Share API failed:', err)
      }
    }
  }

  showFallbackShare(title, text, url)
  return false
}

const showFallbackShare = (title, text, url) => {
  const overlay = document.createElement('div')
  overlay.style.cssText = `
    position: fixed; inset: 0; background: rgba(0,0,0,0.6);
    z-index: 9999; display: flex; align-items: center; justify-content: center;
    animation: fadeIn 0.2s ease;
  `

  const panel = document.createElement('div')
  panel.style.cssText = `
    background: #fff; border-radius: 16px; padding: 24px;
    width: 300px; text-align: center; box-shadow: 0 8px 32px rgba(0,0,0,0.2);
    animation: slideUp 0.3s ease;
  `

  panel.innerHTML = `
    <div style="font-size: 16px; font-weight: 600; margin-bottom: 16px; color: #333;">分享到</div>
    <div style="display: flex; justify-content: center; margin-bottom: 16px;">
      <canvas id="share-qrcode" width="160" height="160"></canvas>
    </div>
    <div style="font-size: 12px; color: #999; margin-bottom: 12px;">扫码访问</div>
    <div style="background: #f5f5f5; border-radius: 8px; padding: 10px; margin-bottom: 12px; word-break: break-all; font-size: 12px; color: #666;">${url}</div>
    <div style="display: flex; gap: 8px;">
      <button id="copy-btn" style="flex: 1; padding: 10px; background: linear-gradient(135deg, #4CAF50, #66BB6A); color: #fff; border: none; border-radius: 8px; cursor: pointer; font-size: 14px;">复制链接</button>
      <button id="close-btn" style="flex: 1; padding: 10px; background: #f5f5f5; color: #666; border: none; border-radius: 8px; cursor: pointer; font-size: 14px;">关闭</button>
    </div>
  `

  overlay.appendChild(panel)
  document.body.appendChild(overlay)

  const style = document.createElement('style')
  style.textContent = `
    @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
    @keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
  `
  document.head.appendChild(style)

  setTimeout(() => {
    const canvas = document.getElementById('share-qrcode')
    if (canvas) {
      QRCode.toCanvas(canvas, url, {
        width: 160,
        margin: 1,
        color: { dark: '#333333', light: '#ffffff' }
      })
    }
  }, 50)

  panel.querySelector('#copy-btn').addEventListener('click', async () => {
    try {
      await navigator.clipboard.writeText(url)
      showToast('链接已复制')
    } catch {
      const textarea = document.createElement('textarea')
      textarea.value = url
      document.body.appendChild(textarea)
      textarea.select()
      document.execCommand('copy')
      document.body.removeChild(textarea)
      showToast('链接已复制')
    }
  })

  const close = () => {
    overlay.remove()
    style.remove()
  }

  panel.querySelector('#close-btn').addEventListener('click', close)
  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) close()
  })
}

export const copyText = async (text) => {
  try {
    await navigator.clipboard.writeText(text)
    showToast('已复制到剪贴板')
    return true
  } catch {
    const textarea = document.createElement('textarea')
    textarea.value = text
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand('copy')
    document.body.removeChild(textarea)
    showToast('已复制到剪贴板')
    return true
  }
}
