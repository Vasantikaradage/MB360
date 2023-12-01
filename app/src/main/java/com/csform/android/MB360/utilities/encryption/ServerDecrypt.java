package com.csform.android.MB360.utilities.encryption;

import android.util.Base64;

import com.csform.android.MB360.utilities.LogMyBenefits;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ServerDecrypt {

    public static String decrypt(String cipherText, String passPhrase) {
        byte[] array = Base64.decode(cipherText, Base64.DEFAULT);
        byte[] salt = Arrays.copyOfRange(array, 0, 32);
        byte[] rgbIV = Arrays.copyOfRange(array, 48, 64);
        byte[] array2 = Arrays.copyOfRange(array, 64, array.length);
        try {
            byte[] bytes = getderiveBytes(passPhrase, salt, 1000);
            Cipher rijndaelManaged = Cipher.getInstance("AES/CBC/PKCS7Padding");
            rijndaelManaged.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bytes, "AES"), new IvParameterSpec(rgbIV));
            byte[] array3 = rijndaelManaged.doFinal(array2);
            return new String(array3, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LogMyBenefits.e("CRYPTO", "error", e);
            return "";
        }
    }

    public static byte[] getderiveBytes(String passPhrase, byte[] salt, int iteration) throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        int keyLength = 32; // in bytes
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iteration, keyLength * 8);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        return keyBytes;
    }

}
