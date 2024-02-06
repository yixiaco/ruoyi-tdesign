import CryptoJS from 'crypto-js';

type WordArray = CryptoJS.lib.WordArray;
type CipherParams = CryptoJS.lib.CipherParams;
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
 * @param iv
 * @param mode
 * @param padding
 */
export function encryptWithAes(
  message: string,
  aesKey: WordArray,
  iv?: WordArray,
  mode = CryptoJS.mode.ECB,
  padding = CryptoJS.pad.Pkcs7,
) {
  const encrypted = CryptoJS.AES.encrypt(message, aesKey, {
    iv,
    mode,
    padding,
  });
  return encrypted.toString();
}

/**
 * 使用密钥对数据进行解密
 * @param message
 * @param aesKey
 * @param iv
 * @param mode
 * @param padding
 */
export function decryptWithAes(
  message: string,
  aesKey: WordArray,
  iv?: WordArray,
  mode = CryptoJS.mode.ECB,
  padding = CryptoJS.pad.Pkcs7,
) {
  const decrypted = CryptoJS.AES.decrypt(message, aesKey, {
    iv,
    mode,
    padding,
  });
  return decrypted.toString(CryptoJS.enc.Utf8);
}

/**
 * 使用密钥对数据进行加密
 * @param message
 * @param aesKey
 * @param iv
 * @param mode
 * @param padding
 */
export function encryptWithAesHex(
  message: WordArray | string,
  aesKey: WordArray,
  iv?: WordArray | string,
  mode = CryptoJS.mode.ECB,
  padding = CryptoJS.pad.Pkcs7,
) {
  if (typeof iv === 'string') {
    iv = CryptoJS.enc.Hex.parse(iv);
  }
  if (typeof aesKey === 'string') {
    aesKey = CryptoJS.enc.Hex.parse(aesKey);
  }
  const encrypted = CryptoJS.AES.encrypt(message, aesKey, {
    iv,
    mode,
    padding,
  });
  return encrypted.toString(CryptoJS.format.Hex);
}

/**
 * 使用密钥对数据进行解密
 * @param message 加密文本
 * @param aesKey key
 * @param iv 偏移向量
 * @param mode
 * @param padding
 */
export function decryptWithAesHex(
  message: CipherParams | string,
  aesKey: WordArray | string,
  iv?: WordArray | string,
  mode = CryptoJS.mode.ECB,
  padding = CryptoJS.pad.Pkcs7,
) {
  if (typeof iv === 'string') {
    iv = CryptoJS.enc.Hex.parse(iv);
  }
  if (typeof aesKey === 'string') {
    aesKey = CryptoJS.enc.Hex.parse(aesKey);
  }
  if (typeof message === 'string') {
    message = CryptoJS.lib.CipherParams.create({
      ciphertext: CryptoJS.enc.Hex.parse(message),
    });
  }
  const decrypted = CryptoJS.AES.decrypt(message, aesKey, {
    iv,
    mode,
    padding,
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
