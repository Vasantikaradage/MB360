package com.csform.android.MB360.utilities.encryption;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class AesEncryption {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = KEY_LENGTH / 8;
    private static final int IV_LENGTH = 16;

    private static byte[] generateSalt(String cipherText) {

        byte[] array = Base64.getDecoder().decode(cipherText);

        byte[] salt = Arrays.copyOfRange(array, 0, 32);

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    private static SecretKey generateSecretKey(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String encrypt(String plaintext, char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        byte[] salt = generateSalt(plaintext);
        byte[] iv = generateIV();

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(password, salt), new IvParameterSpec(iv));

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        byte[] result = new byte[SALT_LENGTH + IV_LENGTH + ciphertext.length];
        System.arraycopy(salt, 0, result, 0, SALT_LENGTH);
        System.arraycopy(iv, 0, result, SALT_LENGTH, IV_LENGTH);
        System.arraycopy(ciphertext, 0, result, SALT_LENGTH + IV_LENGTH, ciphertext.length);

        return Base64.getEncoder().encodeToString(result);
    }

    public static String decrypt(String ciphertext, char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        byte[] decoded = Base64.getDecoder().decode(ciphertext);

        byte[] salt = Arrays.copyOfRange(decoded, 0, SALT_LENGTH);
        byte[] iv = Arrays.copyOfRange(decoded, SALT_LENGTH, SALT_LENGTH + IV_LENGTH);
        byte[] encrypted = Arrays.copyOfRange(decoded, SALT_LENGTH + IV_LENGTH, decoded.length);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(password, salt), new IvParameterSpec(iv));

        byte[] plaintext = cipher.doFinal(encrypted);

        return new String(plaintext);
    }
}

