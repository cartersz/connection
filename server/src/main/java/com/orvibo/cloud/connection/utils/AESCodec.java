package com.orvibo.cloud.connection.utils;

import com.orvibo.cloud.connection.server.constants.Constants;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AESCodec {

	private static final String KEY_ALGORITHM = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	private static final Logger logger = LoggerFactory.getLogger(AESCodec.class);

	private static Key toKey(byte[] key) {
		// 生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}

	/**
	 * 加密
	 * @param content
	 * @param key
	 * @param contentCharset
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String content, String key, String contentCharset) throws Exception {
		byte[] byteContent = content.getBytes(contentCharset);
		return encrypt(byteContent, key);
	}

	public static byte[] encrypt(byte[] content, String key) throws Exception {
		// 实例化
		Key k = toKey(key.getBytes());
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		// 使用密钥初始化，设置为加密模式
		// Cipher.ENCRYPT_MODE
		cipher.init(Cipher.ENCRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(content);
	}

	/**
	 * 解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		// 还原密钥
		Key k = toKey(key.getBytes());
		// 实例化
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		// 使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}

	/**
	 * 加密后的十六进制字符串
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encrypt2String(byte[] content, String key) throws Exception {
		byte[] encryptResult = encrypt(content, key);
		return new String(Hex.encodeHex(encryptResult, false));
	}

//该方法不抛出异常
//	public static String encrypt2String(byte[] content, String password) throws Exception {
//		try {
//			byte[] encryptResult = encrypt(content, password);
//			return new String(Hex.encodeHex(encryptResult, false));
//		} catch (Exception e) {
//			logger.error("AESCodec byte array encrypt to byte array failed.", e);
//		}
//		return "";
//	}

	/**
	 * 将密文的字符串二进制流解密成明文字符串
	 * @param content
	 * @param key
	 * @param contentCharset
	 * @return
	 */
	public static String decrypt2String(byte[] content, String key, String contentCharset) throws Exception{
		byte[] ret = decrypt(content, key);
		return new String(ret, contentCharset);
	}

//不抛出异常
//	public static String decrypt2String(byte[] content, String key, String charset) {
//		try {
//			byte[] ret = decrypt(content, key);
//			return new String(ret, charset);
//		} catch (Exception e) {
//			logger.error("AESCodec byte array decrypt to string failed. content={}, key={}, charset={}", content, key, charset, e);
//		}
//		return "";
//	}

	public static void main(String[] args) throws Exception {
		String content = "test";
		String password = "khggd54865SNJHGF";
		System.out.println("加密前：" + content);
		String  encryptResultStr = encrypt2String(content.getBytes(Constants.CHARSET_UTF_8), password);
//		String encryptResultStr = new String(Hex.encodeHex(encryptResult, false));
		System.out.println("加密后：" + encryptResultStr );
		System.out
				.println("解密后："
						+ decrypt2String(
//								parseHexStr2Byte("DAE03C3BD80D498CA4C5897F09C4B48B"),
								Hex.decodeHex("DAE03C3BD80D498CA4C5897F09C4B48B".toCharArray()),
								password, Constants.CHARSET_UTF_8));
	}
}