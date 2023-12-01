package com.csform.android.MB360.utilities.encryption;

import android.util.Base64;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.utilities.LogMyBenefits;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCrypto {


    public static String Decrypt(String cipherText, String passPhrase) {
        byte[] array = Base64.decode(cipherText, Base64.DEFAULT);
        byte[] salt = Arrays.copyOfRange(array, 0, 32);
        byte[] rgbIV = Arrays.copyOfRange(array, 32, 64);
        byte[] array2 = Arrays.copyOfRange(array, 64, array.length);
        try {
            byte[] bytes = Rfc2898DeriveBytes.deriveKey(passPhrase, salt, 1000);
            bytes = Arrays.copyOfRange(bytes, 0, 32);
            Cipher rijndaelManaged = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(bytes, "AES");
            rijndaelManaged.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(rgbIV));
            byte[] array3 = rijndaelManaged.doFinal(array2);
            return new String(array3, "UTF-8");
        } catch (Exception e) {
            LogMyBenefits.e("CRYPTO", "DECRYPT ERROR: ", e);
            e.printStackTrace();
            return "";
        }
    }


    public static String Encrypt(String plainText, String passPhrase) {
        byte[] array = Generate256BitsOfRandomEntropy();
        byte[] array2 = Generate256BitsOfRandomEntropy();
        byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] bytes2 = Rfc2898DeriveBytes.deriveKey(passPhrase, array, 1000);
            bytes2 = Arrays.copyOfRange(bytes2, 0, 32);

            SecretKeySpec secretKeySpec = new SecretKeySpec(bytes2, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(array2);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(bytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(array);
            outputStream.write(array2);
            outputStream.write(encrypted);
            byte[] inArray = outputStream.toByteArray();
            outputStream.close();
            return printBase64Binary(inArray);

        } catch (Exception e) {
            LogMyBenefits.e("CRYPTO", "ENCRYPT ERROR: ", e);
            return "";
        }
    }

    private static byte[] Generate256BitsOfRandomEntropy() {
        byte[] randomBytes = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    public static String printBase64Binary(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
