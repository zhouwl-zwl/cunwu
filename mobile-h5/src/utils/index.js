export const formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''
  const d = typeof date === 'string' ? new Date(date) : date
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

export const formatRelativeTime = (date) => {
  if (!date) return ''
  const now = new Date()
  const d = typeof date === 'string' ? new Date(date) : date
  
  const diff = now.getTime() - d.getTime()
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const week = 7 * day
  const month = 30 * day
  
  if (diff < minute) return '刚刚'
  if (diff < hour) return `${Math.floor(diff / minute)}分钟前`
  if (diff < day) return `${Math.floor(diff / hour)}小时前`
  if (diff < week) return `${Math.floor(diff / day)}天前`
  if (diff < month) return `${Math.floor(diff / week)}周前`
  
  return formatDate(date, 'MM-DD')
}

export const formatMoney = (amount, decimals = 2) => {
  if (amount === null || amount === undefined) return '0.00'
  const num = Number(amount)
  if (isNaN(num)) return '0.00'
  
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals
  })
}

export const formatNumber = (num) => {
  if (num === null || num === undefined) return '0'
  const n = Number(num)
  if (isNaN(n)) return '0'
  
  if (n >= 100000000) {
    return (n / 100000000).toFixed(2) + '亿'
  }
  if (n >= 10000) {
    return (n / 10000).toFixed(2) + '万'
  }
  
  return n.toString()
}

export const validatePhone = (phone) => {
  const reg = /^1[3-9]\d{9}$/
  return reg.test(phone)
}

export const validateEmail = (email) => {
  const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  return reg.test(email)
}

export const validateIdCard = (idCard) => {
  const reg = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/
  return reg.test(idCard)
}

export const validatePassword = (password) => {
  if (!password || password.length < 6) return { valid: false, message: '密码长度不少于6位' }
  if (!/[a-zA-Z]/.test(password) && !/[0-9]/.test(password)) {
    return { valid: false, message: '密码需要包含字母或数字' }
  }
  return { valid: true, message: '' }
}

export const validateUsername = (username) => {
  if (!username || username.trim().length === 0) return { valid: false, message: '用户名不能为空' }
  if (username.trim().length < 2) return { valid: false, message: '用户名长度不少于2位' }
  if (!/^[\u4e00-\u9fa5a-zA-Z0-9_]+$/.test(username)) {
    return { valid: false, message: '用户名只能包含中文、字母、数字和下划线' }
  }
  return { valid: true, message: '' }
}

export const debounce = (fn, delay = 300) => {
  let timer = null
  return function (...args) {
    clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}

export const throttle = (fn, delay = 300) => {
  let lastTime = 0
  return function (...args) {
    const now = Date.now()
    if (now - lastTime >= delay) {
      lastTime = now
      fn.apply(this, args)
    }
  }
}

export const getQueryParam = (key, url = window.location.href) => {
  const params = new URLSearchParams(new URL(url).search)
  return params.get(key)
}

export const setQueryParam = (key, value, url = window.location.href) => {
  const urlObj = new URL(url)
  urlObj.searchParams.set(key, value)
  return urlObj.toString()
}

export const removeQueryParam = (key, url = window.location.href) => {
  const urlObj = new URL(url)
  urlObj.searchParams.delete(key)
  return urlObj.toString()
}

export const deepClone = (obj) => {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime())
  if (obj instanceof Array) return obj.map(item => deepClone(item))
  if (typeof obj === 'object') {
    const cloned = {}
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        cloned[key] = deepClone(obj[key])
      }
    }
    return cloned
  }
}

export const generateId = () => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2, 9)
}

export const getStorage = (key, defaultValue = null) => {
  try {
    const value = localStorage.getItem(key)
    return value ? JSON.parse(value) : defaultValue
  } catch {
    return defaultValue
  }
}

export const setStorage = (key, value) => {
  try {
    localStorage.setItem(key, JSON.stringify(value))
    return true
  } catch {
    return false
  }
}

export const removeStorage = (key) => {
  try {
    localStorage.removeItem(key)
    return true
  } catch {
    return false
  }
}

export const getCookie = (name) => {
  const cookies = document.cookie.split(';')
  for (const cookie of cookies) {
    const [key, value] = cookie.trim().split('=')
    if (key === name) return decodeURIComponent(value)
  }
  return null
}

export const setCookie = (name, value, options = {}) => {
  let cookie = `${name}=${encodeURIComponent(value)}`
  
  if (options.expires) {
    const date = new Date(options.expires)
    cookie += `; expires=${date.toUTCString()}`
  }
  
  if (options.path) {
    cookie += `; path=${options.path}`
  }
  
  if (options.domain) {
    cookie += `; domain=${options.domain}`
  }
  
  if (options.secure) {
    cookie += '; secure'
  }
  
  document.cookie = cookie
}

export const removeCookie = (name, options = {}) => {
  setCookie(name, '', {
    ...options,
    expires: new Date(0)
  })
}

export const isObjectEmpty = (obj) => {
  return !obj || Object.keys(obj).length === 0
}

export const isArrayEmpty = (arr) => {
  return !arr || arr.length === 0
}

export const trimObject = (obj) => {
  if (!obj || typeof obj !== 'object') return obj
  
  const result = {}
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const value = obj[key]
      if (typeof value === 'string') {
        result[key] = value.trim()
      } else {
        result[key] = value
      }
    }
  }
  return result
}

export const pick = (obj, keys) => {
  if (!obj || !keys || !Array.isArray(keys)) return {}
  
  const result = {}
  for (const key of keys) {
    if (key in obj) {
      result[key] = obj[key]
    }
  }
  return result
}

export const omit = (obj, keys) => {
  if (!obj || !keys || !Array.isArray(keys)) return { ...obj }
  
  const result = { ...obj }
  for (const key of keys) {
    delete result[key]
  }
  return result
}

export const chunk = (arr, size) => {
  if (!arr || !Array.isArray(arr) || size <= 0) return []
  
  const chunks = []
  for (let i = 0; i < arr.length; i += size) {
    chunks.push(arr.slice(i, i + size))
  }
  return chunks
}

export const unique = (arr, key) => {
  if (!arr || !Array.isArray(arr)) return []
  
  if (key) {
    const seen = new Set()
    return arr.filter(item => {
      const value = item[key]
      if (seen.has(value)) return false
      seen.add(value)
      return true
    })
  }
  
  return [...new Set(arr)]
}

export const sortBy = (arr, key, order = 'asc') => {
  if (!arr || !Array.isArray(arr)) return []
  
  return [...arr].sort((a, b) => {
    const valA = a[key]
    const valB = b[key]
    
    if (valA < valB) return order === 'asc' ? -1 : 1
    if (valA > valB) return order === 'asc' ? 1 : -1
    return 0
  })
}

export const filterBy = (arr, predicate) => {
  if (!arr || !Array.isArray(arr) || typeof predicate !== 'function') return []
  
  return arr.filter(predicate)
}

export const mapBy = (arr, key) => {
  if (!arr || !Array.isArray(arr)) return {}
  
  return arr.reduce((acc, item) => {
    acc[item[key]] = item
    return acc
  }, {})
}

export const groupBy = (arr, key) => {
  if (!arr || !Array.isArray(arr)) return {}
  
  return arr.reduce((acc, item) => {
    const groupKey = item[key]
    if (!acc[groupKey]) {
      acc[groupKey] = []
    }
    acc[groupKey].push(item)
    return acc
  }, {})
}

export const sumBy = (arr, key) => {
  if (!arr || !Array.isArray(arr)) return 0
  
  return arr.reduce((sum, item) => {
    return sum + (Number(item[key]) || 0)
  }, 0)
}

export const averageBy = (arr, key) => {
  if (!arr || !Array.isArray(arr) || arr.length === 0) return 0
  
  return sumBy(arr, key) / arr.length
}

export const maxBy = (arr, key) => {
  if (!arr || !Array.isArray(arr) || arr.length === 0) return null
  
  return arr.reduce((max, item) => {
    const val = Number(item[key])
    return val > max ? val : max
  }, -Infinity)
}

export const minBy = (arr, key) => {
  if (!arr || !Array.isArray(arr) || arr.length === 0) return null
  
  return arr.reduce((min, item) => {
    const val = Number(item[key])
    return val < min ? val : min
  }, Infinity)
}

export const flatten = (arr) => {
  if (!arr || !Array.isArray(arr)) return []
  
  return arr.reduce((acc, item) => {
    return acc.concat(Array.isArray(item) ? flatten(item) : item)
  }, [])
}

export const shuffle = (arr) => {
  if (!arr || !Array.isArray(arr)) return []
  
  const result = [...arr]
  for (let i = result.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1))
    ;[result[i], result[j]] = [result[j], result[i]]
  }
  return result
}

export const sleep = (ms) => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

export const safeParse = (str, defaultValue = null) => {
  try {
    return JSON.parse(str)
  } catch {
    return defaultValue
  }
}

export const safeStringify = (obj, defaultValue = '{}') => {
  try {
    return JSON.stringify(obj)
  } catch {
    return defaultValue
  }
}

export const getType = (value) => {
  return Object.prototype.toString.call(value).slice(8, -1).toLowerCase()
}

export const isTypeOf = (value, type) => {
  return getType(value) === type.toLowerCase()
}

export const isFunction = (value) => {
  return typeof value === 'function'
}

export const isPromise = (value) => {
  return value instanceof Promise || (value && typeof value.then === 'function')
}

export const tryCatch = (fn, catchFn = () => {}) => {
  try {
    return fn()
  } catch (error) {
    catchFn(error)
    return null
  }
}

export const tryCatchAsync = async (fn, catchFn = () => {}) => {
  try {
    return await fn()
  } catch (error) {
    catchFn(error)
    return null
  }
}

export const goBack = (router, fallbackPath = '/') => {
  try {
    const routeDepth = router.currentRoute.value?.matched?.length || 0
    const historyLength = window.history.length || 0
    
    if (routeDepth > 1 || historyLength > 1) {
      router.back().catch(() => {
        router.push(fallbackPath)
      })
    } else {
      router.push(fallbackPath)
    }
  } catch (error) {
    router.push(fallbackPath)
  }
}

export const createGoBack = (router, fallbackPath = '/') => {
  return () => goBack(router, fallbackPath)
}