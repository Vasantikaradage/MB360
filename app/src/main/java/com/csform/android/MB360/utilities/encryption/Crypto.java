package com.csform.android.MB360.utilities.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Crypto {

    private static final int KEY_SIZE = 256;
    private static final int SALT_SIZE = 32;
    private static final int IV_SIZE = 16;
    private static final int ITERATIONS = 1000;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static String encrypt(String plainText, String passPhrase) throws Exception {
        byte[] salt = generateRandomEntropy(SALT_SIZE);
        byte[] iv = generateRandomEntropy(IV_SIZE);
        byte[] passwordBytes = passPhrase.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec secretKeySpec = generateSecretKey(passwordBytes, salt);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(salt);
        outputStream.write(iv);
        outputStream.write(encrypted);

        byte[] inArray = outputStream.toByteArray();
        outputStream.close();

        return Base64.getEncoder().encodeToString(inArray);
    }

    public static String decrypt(String cipherText, String passPhrase) throws Exception {
        byte[] inArray = Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = ByteBuffer.wrap(inArray);

        byte[] salt = new byte[SALT_SIZE];
        buffer.get(salt);

        byte[] iv = new byte[IV_SIZE];
        buffer.get(iv);

        byte[] encrypted = new byte[buffer.remaining()];
        buffer.get(encrypted);

        byte[] passwordBytes = passPhrase.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = generateSecretKey(passwordBytes, salt);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec generateSecretKey(byte[] passwordBytes, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(new String(passwordBytes, StandardCharsets.UTF_8).toCharArray(), salt, ITERATIONS, KEY_SIZE);
        SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOfRange(SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM).generateSecret(spec).getEncoded(), 0, KEY_SIZE / 8), "AES");
        return secretKeySpec;
    }

    private static byte[] generateRandomEntropy(int size) {
        byte[] array = new byte[size];
        new SecureRandom().nextBytes(array);
        return array;
    }
}