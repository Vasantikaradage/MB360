package com.csform.android.MB360.utilities.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class ChatGptCrypto {
    // This constant is used to determine the keysize of the encryption algorithm in bits.
    // We divide this by 8 within the code below to get the equivalent number of bytes.
    private static final int KEYSIZE = 256;

    // This constant determines the number of iterations for the password bytes generation function.
    private static final int DERIVATION_ITERATIONS = 1000;

    public static String encrypt(String plainText, String passPhrase) throws Exception {
        // Salt and IV is randomly generated each time, but is preprended to encrypted cipher text
        // so that the same Salt and IV values can be used when decrypting.
        byte[] salt = generate256BitsOfRandomEntropy();
        byte[] iv = generate256BitsOfRandomEntropy();
        byte[] plainTextBytes = plainText.getBytes("UTF-8");

        PBEKeySpec pbeKeySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, DERIVATION_ITERATIONS, KEYSIZE);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] cipherBytes = cipher.doFinal(plainTextBytes);

        byte[] cipherTextBytes = new byte[salt.length + iv.length + cipherBytes.length];
        System.arraycopy(salt, 0, cipherTextBytes, 0, salt.length);
        System.arraycopy(iv, 0, cipherTextBytes, salt.length, iv.length);
        System.arraycopy(cipherBytes, 0, cipherTextBytes, salt.length + iv.length, cipherBytes.length);

        return Base64.getEncoder().encodeToString(cipherTextBytes);
    }


    private static byte[] generate256BitsOfRandomEntropy() {
        byte[] randomBytes = new byte[32]; // 32 Bytes will give us 256 bits.
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }
}

