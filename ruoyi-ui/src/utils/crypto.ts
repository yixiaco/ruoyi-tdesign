import CryptoJS from 'crypto-js';

type WordArray = CryptoJS.lib.WordArray;
// type CipherParams = CryptoJS.lib.CipherParams;
// type X64Word = CryptoJS.x64.Word;

/**
 * 随机生成32位的字符串
 * @param length
 */
function generateRandomString(length = 32) {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  const charactersLength = characters.length;
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}

/**
 * 随机生成aes 密钥
 */
export function generateAesKey() {
  return CryptoJS.enc.Utf8.parse(generateRandomString());
}

/**
 * 使用密钥对数据进行加密
 * @param message
 * @param aesKey
 */
export function encryptWithAes(message: string, aesKey: WordArray) {
  const encrypted = CryptoJS.AES.encrypt(message, aesKey, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  });
  return encrypted.toString();
}

/**
 * 使用密钥对数据进行解密
 * @param message
 * @param aesKey
 */
export function decryptWithAes(message: string, aesKey: WordArray) {
  const decrypted = CryptoJS.AES.decrypt(message, aesKey, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  });
  return decrypted.toString(CryptoJS.enc.Utf8);
}

/**
 * 加密base64
 */
export function encryptBase64(str: WordArray) {
  return CryptoJS.enc.Base64.stringify(str);
}

/**
 * 解密base64
 */
export function decryptBase64(str: string) {
  return CryptoJS.enc.Base64.parse(str);
}

/**
 * md5哈希算法
 */
export function MD5(message: WordArray | string, cfg?: object) {
  return CryptoJS.MD5(message, cfg).toString();
}

/**
 * 数据签名-哈希算法
 */
export function hmacMD5(message: WordArray, key: WordArray | string) {
  return CryptoJS.HmacMD5(message, key).toString(CryptoJS.enc.Utf8);
}

/**
 * 16进制
 */
export function encodeHex(wordArray: WordArray) {
  return CryptoJS.enc.Hex.stringify(wordArray);
}

/**
 * 16进制
 */
export function decodeHex(str: string) {
  return CryptoJS.enc.Hex.parse(str).words;
}
