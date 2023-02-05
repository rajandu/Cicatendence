package com.cicattendance;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class textEnc {

    private static String ALGORITHM = "AES";

    private static String key = "AESEncryptionKey";

    public  String encrypt(String value) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return  android.util.Base64.encode(encryptedByteValue, android.util.Base64.DEFAULT).toString();

    }

    public  String decrypt(String value) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedValue = android.util.Base64.decode(value, android.util.Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decodedValue);
        return new String(decryptedByteValue, "utf-8");
    }

}
