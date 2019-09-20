/**
 * .
 */

package com.oseasy.util.common.utils.rsa;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * DES加密工具类.
 * @author chenhao
 */
@SuppressWarnings("restriction")
public class DESUtil {
    private static final String key_ = "apabi*****";
    public static final byte[] DESkey = key_.getBytes();// 设置密钥，略去
    private static final String DESIV_ = "ISO10126";
    public static final byte[] DESIV = DESIV_.getBytes();// 设置密钥，略去
    static AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
    private static Key key = null;

    public DESUtil(byte[] deskey, byte[] desiv) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(deskey);// 设置密钥参数
        iv = new IvParameterSpec(desiv);// 设置向量
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
        key = keyFactory.generateSecret(keySpec);// 得到密钥对象
    }

    public String encrypt(String data) throws Exception {
        Cipher enCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// 得到加密对象Cipher
        enCipher.init(Cipher.ENCRYPT_MODE, key);// 设置工作模式为加密模式，给出密钥和向量
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(pasByte);
    }

    public static void main(String[] args) throws Exception {
        DESUtil tools = new DESUtil(DESUtil.DESkey, DESUtil.DESIV);
        System.out.println("加密:" + tools.encrypt("testing1$apabi$2011041812"));
        System.out.println("加密:" + tools.encrypt("testing1$apabi$2011041812:20"));
    }
}
