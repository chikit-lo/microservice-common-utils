package io.github.chikitlo.common.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

/**
 * Java Simplified Encryption Encryptor Utils
 * <p>
 * Utility class for performing PBE(password-based) encryption/decryption using Jasypt.
 * Provides factory-style static methods and common algorithm constants.
 * </p>
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 17:30
 */
public final class JasyptUtils {
    public static final String PBE_ALGORITHMS_MD5_DES = "PBEWithMD5AndDES";
    public static final String PBE_ALGORITHMS_HMAC_AES_128 = "PBEWithHmacSHA512AndAES_128";
    public static final String PBE_ALGORITHMS_HMAC_AES_256 = "PBEWithHmacSHA512AndAES_256";
    public static final String DEFAULT_ALGORITHM = PBE_ALGORITHMS_HMAC_AES_256;

    private JasyptUtils() {
    }

    /**
     * Encrypt a plaintext with the specified algorithm and password.
     *
     * @param plainText
     * @param algorithm
     * @param password
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:53
     */
    public static String encrypt(String plainText, String algorithm, String password) {
        StandardPBEStringEncryptor encryptor = createEncryptor(algorithm, password);
        return encryptor.encrypt(plainText);
    }

    /**
     * Decrypt a ciphertext with the specified algorithm and password.
     *
     * @param cipherText
     * @param algorithm
     * @param password
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:54
     */
    public static String decrypt(String cipherText, String algorithm, String password) {
        StandardPBEStringEncryptor encryptor = createEncryptor(algorithm, password);
        return encryptor.decrypt(cipherText);
    }

    /**
     * Encrypt a plaintext using the {@link #DEFAULT_ALGORITHM}.
     *
     * @param plainText
     * @param password
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:54
     */
    public static String encrypt(String plainText, String password) {
        return encrypt(plainText, DEFAULT_ALGORITHM, password);
    }

    /**
     * Decrypt a ciphertext using the {@link #DEFAULT_ALGORITHM}.
     *
     * @param cipherText
     * @param password
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:55
     */
    public static String decrypt(String cipherText, String password) {
        return decrypt(cipherText, DEFAULT_ALGORITHM, password);
    }

    /**
     * Create and configure a new Jasypt encryptor instance.
     *
     * @param algorithm
     * @param password
     * @return org.jasypt.encryption.pbe.StandardPBEStringEncryptor
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:24
     */
    private static StandardPBEStringEncryptor createEncryptor(String algorithm, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(algorithm);
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor;
    }
}