package com.csform.android.MB360.utilities.encryption;

import android.util.Base64;
import android.util.Log;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.utilities.LogMyBenefits;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class BaseEncryption {

    public static String encrypt(String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException, InvalidKeySpecException {

        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.PRIVATE_KEY, generateKey(BuildConfig.BASIC_AUTH_KEY));
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);


    }


    public static String decrypt(String cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.PUBLIC_KEY, secret);
        byte[] decode = Base64.decode(cipherText, Base64.NO_WRAP);
        String decryptString = new String(cipher.doFinal(decode), "UTF-8");
        return decryptString;
    }

    public static SecretKey generateKey(String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeySpec secret;
        secret = new SecretKeySpec(key.getBytes(), "AES");
        return secret;
    }

    public static String encryptMessage(String message) throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeySpecException, InvalidAlgorithmParameterException {

       /* final byte[] saltBytes = {0, 1, 2, 3, 4, 5, 6};
        SecretKeyFactory factory = SecretKeyFactory.getInstance("AES");
        PBEKeySpec spec = new PBEKeySpec(BuildConfig.AUTH_ENCRYPT_KEY.toCharArray(), saltBytes, 65536, 128);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        byte[] plaintext = message.getBytes();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.PUBLIC_KEY, secretKey);
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] iv = cipher.getIV();

        return ciphertext.toString();*/

        byte[] sessionKey = BuildConfig.BASIC_AUTH_KEY.getBytes(StandardCharsets.UTF_8); //Where you get this from is beyond the scope of this post
        byte[] iv = BuildConfig.AUTH_ENCRYPT_KEY_IV.getBytes(StandardCharsets.UTF_8); //Ditto
        byte[] plaintext = message.getBytes(StandardCharsets.UTF_8); //Whatever you want to encrypt/decrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        //You can use ENCRYPT_MODE or DECRYPT_MODE
        //** iv	5459353441424358
        //key	3532414233323B5E2421455239343938384F505333573231 **/
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
        byte[] ciphertext = cipher.doFinal(plaintext);
        LogMyBenefits.d("ENCRYPT->", "encryptMessage: " + ciphertext.toString());
        return ciphertext.toString();
    }

}
