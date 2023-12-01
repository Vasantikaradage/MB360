package com.csform.android.MB360.utilities.encryption;

import com.csform.android.MB360.utilities.LogMyBenefits;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ServerCrypto {


    public static String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); //this parameters should not be changed
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = new byte[text.length()];
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            results = cipher.doFinal(decoder.decode(text));
        } catch (Exception e) {
            LogMyBenefits.i("Erron in Decryption", e.toString());
        }
        LogMyBenefits.i("Data", new String(results, "UTF-8"));
        return new String(results, "UTF-8"); // it returns the result as a String
    }

    public static String Encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(results); // it returns the result as a String
    }

}
