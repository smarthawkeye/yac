package io.github.smarthawkeye.core.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * 加密工具类，包含MD5,BASE64,SHA,CRC32
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class CryptoUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * MD5加密
     * @param bytes an array of byte.
     * @return a {@link String} object.
     */
    public static String encodeMD5(final byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    /**
     * MD5加密，默认UTF-8
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public static String encodeMD5(final String str) {
        return encodeMD5(str, DEFAULT_CHARSET);
    }

    /**
     * MD5加密
     * @param str     a {@link String} object.
     * @param charset a {@link String} object.
     * @return a {@link String} object.
     */
    public static String encodeMD5(final String str, final String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeMD5(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SHA加密
     *
     * @param bytes an array of byte.
     * @return a {@link String} object.
     */
    public static String encodeSHA(final byte[] bytes) {
        return DigestUtils.sha512Hex(bytes);
    }

    /**
     * SHA加密
     *
     * @param str     a {@link String} object.
     * @param charset a {@link String} object.
     * @return a {@link String} object.
     */
    public static String encodeSHA(final String str, final String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeSHA(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SHA加密,默认utf-8
     *
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public static String encodeSHA(final String str) {
        return encodeSHA(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64加密
     *
     * @param bytes an array of byte.
     * @return a {@link String} object.
     */
    public static String encodeBASE64(final byte[] bytes) {
        return new String(Base64.encodeBase64String(bytes));
    }

    /**
     * BASE64加密
     *
     * @param str     a {@link String} object.
     * @param charset a {@link String} object.
     * @return a {@link String} object.
     */
    public static String encodeBASE64(final String str, String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeBASE64(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BASE64加密,默认UTF-8
     *
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public static String encodeBASE64(final String str) {
        return encodeBASE64(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64解密,默认UTF-8
     *
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public static String decodeBASE64(String str) {
        return decodeBASE64(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64解密
     *
     * @param str     a {@link String} object.
     * @param charset 字符编码
     * @return a {@link String} object.
     */
    public static String decodeBASE64(String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return new String(Base64.decodeBase64(bytes));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BASE64解密
     *
     * @param bytes
     * @return 解密
     */
    public static String decodeBASE64(byte[] bytes) {
        try {
            return new String(Base64.decodeBase64(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * BASE64解密
     *
     * @param bytes
     * @return 解密
     */
    public static byte[] decodeByteBASE64(byte[] bytes) {
        try {
            return Base64.decodeBase64(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * CRC32字节校验
     *
     * @param bytes an array of byte.
     * @return a {@link String} object.
     */
    public static String crc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return Long.toHexString(crc32.getValue());
    }

    /**
     * CRC32字符串校验
     *
     * @param str     a {@link String} object.
     * @param charset a {@link String} object.
     * @return a {@link String} object.
     */
    public static String crc32(final String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return crc32(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CRC32字符串校验,默认UTF-8编码读取
     *
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public static String crc32(final String str) {
        return crc32(str, DEFAULT_CHARSET);
    }

    /**
     * CRC32流校验
     *
     * @param input a {@link InputStream} object.
     * @return a {@link String} object.
     */
    public static String crc32(InputStream input) {
        CRC32 crc32 = new CRC32();
        CheckedInputStream checkInputStream = null;
        int test = 0;
        try {
            checkInputStream = new CheckedInputStream(input, crc32);
            do {
                test = checkInputStream.read();
            } while (test != -1);
            return Long.toHexString(crc32.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * CRC32文件唯一校验
     *
     * @param file a {@link File} object.
     * @return a {@link String} object.
     */
    public static String crc32(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            return crc32(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * CRC32文件唯一校验
     *
     * @param url a {@link URL} object.
     * @return a {@link String} object.
     */
    public static String crc32(URL url) {
        InputStream input = null;
        try {
            input = url.openStream();
            return crc32(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * AES解密
     *
     * @param content 解密内容
     * @param sKey    解密key，16位长度
     * @param vi      向量iv
     * @throws Exception
     */
    public static byte[] decryptByAes(byte[] content, String sKey, String vi) throws Exception {
        return cryptByAes(content, sKey, vi, Cipher.DECRYPT_MODE);
    }

    /**
     * AES加密
     *
     * @param content 加密内容
     * @param sKey    加密key，16位长度
     * @param vi      向量iv
     * @throws Exception
     */
    public static byte[] encryptByAes(byte[] content, String sKey, String vi) throws Exception {
        return cryptByAes(content, sKey, vi, Cipher.ENCRYPT_MODE);
    }

    /**
     * AES加密
     *
     * @param content    加解密内容
     * @param sKey       加解密key，16位长度
     * @param vi         向量iv
     * @param CRYPT_MODE 加解密参数，加密：Cipher.ENCRYPT_MODE，解密：Cipher.DECRYPT_MODE
     * @throws Exception
     */
    public static byte[] cryptByAes(byte[] content, String sKey, String vi, int CRYPT_MODE) throws Exception {
        try {
            /**
             * 判断Key是否正确
             */
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            /**
             * 判断Key是否为16位
             */
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            /**
             * 向量iv，增加加密算法的强度
             */
            IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
            cipher.init(CRYPT_MODE, skeySpec, iv);
            try {
                /**
                 * AES加解密
                 */
                return cipher.doFinal(content);
            } catch (Exception e) {
                if (CRYPT_MODE == Cipher.ENCRYPT_MODE) {
                    System.out.println("EncryptUtil-->decryptByAesAES加密失败！" + e);
                } else {
                    System.out.println("EncryptUtil-->decryptByAesAES解密失败！" + e);
                }
                return null;
            }
        } catch (Exception ex) {
            System.out.println("EncryptUtil-->decryptByAesAES加解密失败！" + ex);
            return null;
        }
    }

    /**
     * 使用BASE64解码后，使用AES解密
     * @param content 解密内容
     * @param sKey    解密key，16位长度
     * @param vi      向量iv
     * @throws Exception
     */
    public static String decryptByAesAndBASE64(String content, String sKey, String vi) throws Exception {
        return new String(cryptByAes(decodeByteBASE64(content.getBytes()), sKey, vi, Cipher.DECRYPT_MODE));
    }

    /**
     * 使用AES加密，并使用BASE64编码
     * @param content 加密内容
     * @param sKey    加密key，16位长度
     * @param vi      向量iv
     * @throws Exception
     */
    public static String encryptByAesAndBASE64(String content, String sKey, String vi) throws Exception {
        return encodeBASE64(cryptByAes(content.getBytes(), sKey, vi, Cipher.ENCRYPT_MODE));
    }
}