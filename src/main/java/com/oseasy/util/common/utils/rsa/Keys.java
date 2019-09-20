package com.oseasy.util.common.utils.rsa;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/***
 * Java RSA加密算法生成公钥和私钥
 */
public class Keys {
	public static final String KEY_ALGORITHM = "RSA";
//	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY = "RSAPublic-oseasy";
	private static final String PRIVATE_KEY = "RSAPrivate-oseasy";

	private static final String DES_KEY=  "oseasy!9*o$%)";

	public static void main(String[] args) {
		Map<String, Object> keyMap;
		try {
			keyMap = initKey();
			String publicKey = getPublicKey(keyMap);
			System.out.println(publicKey);
			String privateKey = getPrivateKey(keyMap);
			System.out.println(privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		byte[] publicKey = key.getEncoded();
//		System.out.println(publicKey.length);
//		return encryptDES(key.getEncoded());
		return encryptBASE64(key.getEncoded());
	}

	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		byte[] privateKey = key.getEncoded();
//		return encryptDES(key.getEncoded());
		return encryptBASE64(key.getEncoded());
	}

	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/*public static byte[] decryptDES(String data) throws Exception {
	     BASE64Decoder decoder = new BASE64Decoder();
         byte[] buf = decoder.decodeBuffer(data);
         byte[] bt = DesUtil.decrypt(buf,DES_KEY.getBytes());
		return bt;
	}*/

	/*public static String encryptDES(byte[] data) throws Exception {
	        BASE64Decoder decoder = new BASE64Decoder();
	        byte[] buf = decoder.decodeBuffer(new String(data));
	        byte[] bt = DesUtil.decrypt(buf,DES_KEY.getBytes());
	        return new String(bt);
	}*/

	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

}