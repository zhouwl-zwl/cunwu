/**
 * 纯 JS SHA-256 实现（十六进制输出）
 *
 * 为什么不用 crypto.subtle：静态站点以 http 部署时（非 localhost），
 * Web Crypto API 不可用，因此内置纯 JS 实现以保证任何环境都能校验密码。
 *
 * 输入会先做 UTF-8 编码，支持中文密码。
 */

function rightRotate(value, amount) {
  return (value >>> amount) | (value << (32 - amount))
}

function rawSha256(ascii) {
  const mathPow = Math.pow
  const maxWord = mathPow(2, 32)
  const lengthProperty = 'length'
  let i, j
  let result = ''

  const words = []
  const asciiBitLength = ascii[lengthProperty] * 8

  const hash = []
  const k = []
  let primeCounter = k[lengthProperty]

  const isComposite = {}
  for (let candidate = 2; primeCounter < 64; candidate++) {
    if (!isComposite[candidate]) {
      for (i = 0; i < 313; i += candidate) {
        isComposite[i] = candidate
      }
      hash[primeCounter] = (mathPow(candidate, 0.5) * maxWord) | 0
      k[primeCounter++] = (mathPow(candidate, 1 / 3) * maxWord) | 0
    }
  }

  ascii += '\x80'
  while (ascii[lengthProperty] % 64 - 56) ascii += '\x00'
  for (i = 0; i < ascii[lengthProperty]; i++) {
    j = ascii.charCodeAt(i)
    if (j >> 8) return '' // 仅支持 0-255 字符（外部已做 UTF-8 转换）
    words[i >> 2] |= j << ((3 - i) % 4) * 8
  }
  words[words[lengthProperty]] = ((asciiBitLength / maxWord) | 0)
  words[words[lengthProperty]] = (asciiBitLength)

  for (j = 0; j < words[lengthProperty];) {
    const w = words.slice(j, j += 16)
    const oldHash = hash.slice(0)
    for (i = 16; i < 64; i++) {
      w[i] = (rightRotate(w[i - 2], 17) ^ rightRotate(w[i - 2], 19) ^ (w[i - 2] >>> 10)) + w[i - 7] + (rightRotate(w[i - 15], 7) ^ rightRotate(w[i - 15], 18) ^ (w[i - 15] >>> 3)) + w[i - 16] | 0
    }

    let a = hash[0], b = hash[1], c = hash[2], d = hash[3], e = hash[4], f = hash[5], g = hash[6], h = hash[7]

    for (i = 0; i < 64; i++) {
      const maj = (a & b) ^ (a & c) ^ (b & c)
      const s1 = rightRotate(e, 6) ^ rightRotate(e, 11) ^ rightRotate(e, 25)
      const ch = (e & f) ^ (~e & g)
      const temp1 = (h + s1 + ch + k[i] + w[i]) | 0
      const s0 = rightRotate(a, 2) ^ rightRotate(a, 13) ^ rightRotate(a, 22)
      const temp2 = (maj + s0) | 0
      h = g; g = f; f = e; e = (d + temp1) | 0; d = c; c = b; b = a; a = (temp1 + temp2) | 0
    }

    hash[0] = (hash[0] + a) | 0
    hash[1] = (hash[1] + b) | 0
    hash[2] = (hash[2] + c) | 0
    hash[3] = (hash[3] + d) | 0
    hash[4] = (hash[4] + e) | 0
    hash[5] = (hash[5] + f) | 0
    hash[6] = (hash[6] + g) | 0
    hash[7] = (hash[7] + h) | 0
  }

  for (i = 0; i < 8; i++) {
    for (j = 3; j + 1; j--) {
      const b = (hash[i] >> (j * 8)) & 255
      result += ((b < 16) ? '0' : '') + b.toString(16)
    }
  }
  return result
}

/**
 * 计算字符串的 SHA-256（十六进制，UTF-8 安全）
 * @param {string} text
 * @returns {string}
 */
export function sha256Hex(text) {
  const utf8 = unescape(encodeURIComponent(String(text)))
  return rawSha256(utf8)
}
