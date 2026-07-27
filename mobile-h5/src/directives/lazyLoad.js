import { nextTick } from 'vue'

const lazyLoadDirective = {
  mounted(el, binding) {
    const src = binding.value || el.src
    if (!src) return

    el.setAttribute('data-src', src)
    el.removeAttribute('src')
    el.style.opacity = '0'
    el.style.transition = 'opacity 0.3s ease-in'

    if ('IntersectionObserver' in window) {
      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            const img = entry.target
            const dataSrc = img.getAttribute('data-src')
            if (dataSrc) {
              img.src = dataSrc
              img.removeAttribute('data-src')
              img.onload = () => {
                img.style.opacity = '1'
              }
              img.onerror = () => {
                img.style.opacity = '0.5'
                img.style.background = '#f5f5f5'
              }
            }
            observer.unobserve(img)
          }
        })
      }, {
        rootMargin: '100px',
        threshold: 0.01
      })

      observer.observe(el)
      el._lazyObserver = observer
    } else {
      el.src = src
      el.onload = () => {
        el.style.opacity = '1'
      }
    }
  },
  beforeUnmount(el) {
    if (el._lazyObserver) {
      el._lazyObserver.disconnect()
    }
  }
}

export default lazyLoadDirective
