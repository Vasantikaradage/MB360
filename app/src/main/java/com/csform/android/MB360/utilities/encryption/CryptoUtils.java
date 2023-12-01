package com.csform.android.MB360.utilities.encryption;

import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CryptoUtils {

    public static byte[] generateRandomBytes(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String encrypt(String plainText, String passPhrase) throws Exception {
        byte[] array = generateRandomBytes(32);
        byte[] array2 = generateRandomBytes(32);
        byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] salt = generateRandomBytes(16);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1000, 256);
        SecretKeySpec secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(array2));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(array);
        outputStream.write(array2);
        outputStream.write(cipher.doFinal(bytes));
        byte[] inArray = outputStream.toByteArray();
        outputStream.close();
        return Base64.getEncoder().encodeToString(inArray);
    }

    public static String decrypt(String cipherText, String passPhrase) throws Exception {
        byte[] inArray = Base64.getDecoder().decode(cipherText);
        byte[] salt = Arrays.copyOfRange(inArray, 0, 16);
        byte[] array2 = Arrays.copyOfRange(inArray, 16, 48);
        byte[] array = Arrays.copyOfRange(inArray, 48, inArray.length);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1000, 256);
        SecretKeySpec secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(array2));

        byte[] decrypted = cipher.doFinal(array);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}

