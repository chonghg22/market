package kr.co.market.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
@Component
public class CryptoUtil {
    private static Key key = null;
    public static String cryptoAlgorithm;
    public static int cryptoIterationCount;
    public static int cryptoKeyLength;
    public static String cryptoSendbeeKey;

    @Value("${CRYPTO_ALGORITHM}")
    public void setCryptoAlgorithm(String algorithm){
        cryptoAlgorithm = algorithm;
    }

    @Value("${CRYPTO_ITERATION_COUNT}")
    public void setCryptoIterationCount(int iterationCount){
        cryptoIterationCount = iterationCount;
    }

    @Value("${CRYPTO_KEY_LENGTH}")
    public void setCryptoKeyLength(int keyLength){
        cryptoKeyLength = keyLength;
    }

//    @Value("${GIFTTING_CRYPTO_KEY_PATH}")
    public void setCryptoSendbeeKey(String sendbeeKey){ cryptoSendbeeKey = sendbeeKey; }

    public static byte[] hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return hash(password, salt, cryptoIterationCount, cryptoKeyLength, cryptoAlgorithm);
    }

    public static byte[] hash(String password, byte[] salt, int iterationCnt, int keyLen, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCnt, keyLen);
        SecretKeyFactory secretKeyfactory = SecretKeyFactory.getInstance(algorithm);
        return secretKeyfactory.generateSecret(keySpec).getEncoded();
    }

    public static byte[] salt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[64];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
        return salt;
    }

    public static String convertToString(byte[] payload) {
        String result = "";
        byte[] var5 = payload;
        int var4 = payload.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            byte b = var5[var3];
            result = result + b + " ";
        }

        return result.trim();
    }

    public static byte[] toByteArray(String s) {
        String[] arr = s.split(" ");
        byte[] b = new byte[arr.length];

        for(int i = 0; i < arr.length; ++i) {
            b[i] = Byte.parseByte(arr[i]);
        }

        return b;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for(int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var5 = bytes;
        int var4 = bytes.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            byte b = var5[var3];
            sb.append(String.format("%02X", b & 255));
        }

        return sb.toString();
    }

    private static Key getKey() throws Exception {
        return key != null ? key : getKey(cryptoSendbeeKey);
    }

    private static Key getKey(String filename) throws Exception {
        if (key == null) {
            ClassPathResource resource = new ClassPathResource(filename);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(resource.getFile().getPath()));
            key = (Key)in.readObject();
            in.close();
        }
        return key;
    }

    public static String encrypt(String strVal){
        try{
            if (strVal != null && strVal.length() != 0) {
                Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(1, getKey());
                byte[] inputBytes1 = strVal.getBytes("UTF-8");
                byte[] outputBytes1 = cipher.doFinal(inputBytes1);
//                String strOutput = DatatypeConverter.printBase64Binary(outputBytes1);
                String strOutput = "";
                return strOutput;
            } else {
                return "";
            }
        }catch (Exception e){
//            log.error("ERROR ", e);
            return strVal;
        }
    }

    public static String decrypt(String strVal) {
        try{
            if (strVal != null && strVal.length() != 0) {
                Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(2, getKey());
//                byte[] inputBytes1 = DatatypeConverter.parseBase64Binary(strVal);
                byte[] inputBytes1 = null;
                byte[] outputBytes2 = cipher.doFinal(inputBytes1);
                String strResult = new String(outputBytes2, "UTF-8");
                return strResult;
            } else {
                return "";
            }
        }catch (Exception e){
//            log.error("ERROR ", e);
            return strVal;
        }
    }

    public static String getSalt() {

        byte[] encoded = null;
        byte[] randomBytes = null;
        try {
            randomBytes = salt();
            encoded = Base64.getEncoder().encode(randomBytes);

        } catch (NoSuchAlgorithmException e) {
            log.error("getSalt ERROR " + e.getMessage());
        }
        return new String(encoded);
    }

    public static String getPBKDF2(String passwd, String salt) {

        byte[] cryptoPasswd = null;
        try {
            cryptoPasswd = hash(passwd, salt.getBytes());

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("getPBKDF2 ERROR " + e.getMessage());
        }

        return byteArrayToHexString(cryptoPasswd);
    }

    public static String getSHA512(String input){

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    // 암호화 aes 방식
    public static String encAES(String str, String key, String iv) {
        try{
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes(StandardCharsets.UTF_8);

            int len = b.length;
            if (len > keyBytes.length) {
                len = keyBytes.length;
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            Key keySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
            String enStr = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encrypted));

            return enStr;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    // 복호화 aes 방식
    public static String decAES(String enStr, String key, String iv) {
        try{
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes(StandardCharsets.UTF_8);

            int len = b.length;
            if (len > keyBytes.length) {
                len = keyBytes.length;
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            Key keySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            byte[] byteStr = org.apache.commons.codec.binary.Base64.decodeBase64(enStr.getBytes(StandardCharsets.UTF_8));
            String decStr = new String(c.doFinal(byteStr), StandardCharsets.UTF_8);

            return decStr;
        }catch (Exception e){
            e.printStackTrace();
            return  "";
        }

    }

}
