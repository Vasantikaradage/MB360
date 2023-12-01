package com.csform.android.MB360.utilities.encryption;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class BaseEncryptionUtils {


    public static String Encrypt(String plainText, String passPhrase) throws Exception {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        byte[] nonce = new byte[16];
        random.nextBytes(nonce);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1000, 256);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(nonce));

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

        byte[] result = new byte[48 + encrypted.length];
        System.arraycopy(salt, 0, result, 0, 16);
        System.arraycopy(nonce, 0, result, 16, 16);
        System.arraycopy(encrypted, 0, result, 48, encrypted.length);

        return Base64.getEncoder().encodeToString(result);
    }


    public static String Decrypt(String cipherText, String passPhrase) throws Exception {
        byte[] data = Base64.getDecoder().decode(cipherText);

        byte[] salt = new byte[16];
        System.arraycopy(data, 0, salt, 0, 16);

        byte[] nonce = new byte[16];
        System.arraycopy(data, 16, nonce, 0, 16);

        byte[] encrypted = new byte[data.length - 48];
        System.arraycopy(data, 48, encrypted, 0, encrypted.length);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1000, 256);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(nonce));

        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, "UTF-8");
    }

}
