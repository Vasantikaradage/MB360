package com.csform.android.MB360.utilities.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Rfc2898DeriveBytes {
    public static byte[] deriveBytes(String password, byte[] salt, int iterations, int keyLength) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength*2);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = factory.generateSecret(spec);
        return key.getEncoded();
    }

    public static byte[] deriveKey(String passPhrase, byte[] array, int iterations) throws InvalidKeySpecException, NoSuchAlgorithmException {
        // Convert the passPhrase to a char array
        char[] passphraseChars = passPhrase.toCharArray();

        // Create a PBEKeySpec object with the passPhrase, salt, and iteration count
        PBEKeySpec spec = new PBEKeySpec(passphraseChars, array, iterations, 256);

        // Use a SecretKeyFactory to generate a secret key based on the PBEKeySpec
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secretKey = factory.generateSecret(spec);

        // Return the raw bytes of the secret key
        return secretKey.getEncoded();
    }




}
