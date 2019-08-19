package com.cn.base.util;

import com.cn.base.constant.CommonConstant;
import org.apache.commons.lang.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * @author dengyu
 * @desc 密码工具帮助类
 * @date 2018/5/19
 */
public final class PasswordUtil {
    /**
     * 隐藏构造器
     */
    private PasswordUtil() {

    }

    public static final Integer DEFAULT_ITERATIONS = 20000;
    public static final String ALGORITHM = "pbkdf2_sha256";

    /**
     * @param password   密码
     * @param salt       salt
     * @param iterations iterations
     * @return 字符串
     */
    private static String getEncodedHash(String password, String salt, int iterations) {
        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            System.exit(1);
        }
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(Charset.forName(CommonConstant.CHARSET_UTF8)), iterations, 256);
        SecretKey secret = null;
        try {
            secret = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        byte[] rawHash = secret.getEncoded();
        byte[] hashBase64 = Base64.getEncoder().encode(rawHash);
        return new String(hashBase64);
    }

    /**
     * make salt
     *
     * @return String
     */
    private static String getSalt() {
        int length = 12;
        Random rand = new Random();
        char[] rs = new char[length];
        for (int i = 0; i < length; i++) {
            int t = rand.nextInt(3);
            if (t == 0) {
                rs[i] = (char) (rand.nextInt(10) + 48);
            } else if (t == 1) {
                rs[i] = (char) (rand.nextInt(26) + 65);
            } else {
                rs[i] = (char) (rand.nextInt(26) + 97);
            }
        }
        return new String(rs);
    }

    /**
     * rand salt
     * iterations is default 20000
     *
     * @param password password
     * @return String
     */
    private static String encode(String password) {
        return encode(password, getSalt());
    }

    /**
     * rand salt
     *
     * @param password   password
     * @param iterations iterations
     * @return String
     */
    private static String encode(String password, int iterations) {
        return encode(password, getSalt(), iterations);
    }

    /**
     * iterations is default 20000
     *
     * @param password password
     * @param salt     salt
     * @return String
     */
    private static String encode(String password, String salt) {
        return encode(password, salt, DEFAULT_ITERATIONS);
    }

    /**
     * @param password   password
     * @param salt       salt
     * @param iterations iterations
     * @return String
     */
    private static String encode(String password, String salt, int iterations) {
        String hash = getEncodedHash(password, salt, iterations);
        return String.format("%s$%d$%s$%s", ALGORITHM, iterations, salt, hash);
    }

    /**
     * 验证密码是否正确
     *
     * @param password       password
     * @param hashedPassword hashedPassword
     * @return boolean
     */
    public static boolean checkPasswordRight(String password, String hashedPassword) {
        if (hashedPassword == null || password == null) {
            return false;
        }
        String[] parts = hashedPassword.split("\\$");
        if (parts.length != 4) {
            // wrong hash format
            return false;
        }
        Integer iterations = Integer.parseInt(parts[1]);
        String salt = parts[2];
        String hash = encode(password, salt, iterations);

        return hash.equals(hashedPassword);
    }

    /**
     * @param password password
     * @return String
     */
    public static String generateEncryptPassword(String password) {
        if (StringUtils.isNotEmpty(password)) {
            return encode(password, getSalt(), DEFAULT_ITERATIONS);
        } else {
            return password;
        }
    }

}
