package com.cn.base.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @desc: 功能简述: 加密解密工具类
 * @author: zhangwanquan
 * @date: 2019/4/24
 */
public final class SecretUtil {

    private static final String DES_ALGORITHM = "des";

    private SecretUtil() {
    }

    /**
     * 功能简述: 使用DES算法进行加密.
     *
     * @param plainData 明文数据
     * @param key       加密密钥
     * @return 字节数组
     */
    public static byte[] encryptDES(byte[] plainData, String key) {
        return processCipher(plainData, createSecretKey(key), Cipher.ENCRYPT_MODE, DES_ALGORITHM);
    }

    /**
     * 功能简述: 使用DES算法进行解密.
     *
     * @param cipherData 密文数据
     * @param key        解密密钥
     * @return 字节数组
     */
    public static byte[] decryptDES(byte[] cipherData, String key) {
        return processCipher(cipherData, createSecretKey(key), Cipher.DECRYPT_MODE, DES_ALGORITHM);
    }

    /**
     * 功能简述: 根据key创建密钥SecretKey.
     *
     * @param key 生成密钥的key
     * @return 密钥
     */
    private static SecretKey createSecretKey(String key) {
        SecretKey secretKey = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    /**
     * 功能简述: 加密/解密处理流程.
     *
     * @param processData 待处理的数据
     * @param key         提供的密钥
     * @param opsMode     工作模式
     * @param algorithm   使用的算法
     * @return 字节数组
     */
    private static byte[] processCipher(byte[] processData, Key key, int opsMode, String algorithm) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(opsMode, key, new SecureRandom());
            return cipher.doFinal(processData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param param 源值
     * @param key   密钥
     * @return 加密值
     */
    public static String encrypt(String param, String key) {
        return byteArr2HexStr(encryptDES(param.getBytes(), key));
    }

    /**
     * @param param 加密值
     * @param key   密钥
     * @return 源值
     */
    public static String decrypt(String param, String key) {
        return new String(decryptDES(hexStr2ByteArr(param), key));
    }

    /**
     * @param arrB byte
     * @return 字符
     */
    public static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * str2byte
     *
     * @param strIn 输入
     * @return byte
     */
    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String encrypt = SecretUtil.encrypt("sssssssssssss", "dengyu");
        System.out.println(encrypt);
        System.out.println((System.currentTimeMillis() - start));
        String dengyu = SecretUtil.decrypt(encrypt, "dengyu");
        System.out.println(dengyu);
        System.out.println((System.currentTimeMillis() - start));

        dengyu = SecretUtil.decrypt(encrypt, "dengyu");
        System.out.println(dengyu);
        System.out.println((System.currentTimeMillis() - start));
        dengyu = SecretUtil.decrypt(encrypt, "dengyu");
        System.out.println(dengyu);
        System.out.println((System.currentTimeMillis() - start));
        dengyu = SecretUtil.decrypt(encrypt, "dengyu");
        System.out.println(dengyu);
        System.out.println((System.currentTimeMillis() - start));
        dengyu = SecretUtil.decrypt(encrypt, "dengyu");
        System.out.println(dengyu);
        System.out.println((System.currentTimeMillis() - start));
    }

}
