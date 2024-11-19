package com.bark.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

/**
 * @Author Ballen  2024/10/23 15:46
 */
public class AESCodec {
    private static final Charset charset = Charset.forName("UTF-8");

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String encrypt(String data, String key, String iv)throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return Base64Codec
                .encode(aes(data.getBytes(charset), key.getBytes(charset), iv.getBytes(charset), true));
    }

    public String decrypt(String encryptedData, String key, String iv) throws AesDecryptFailedException {
        try {
            return new String(aes(Base64Codec.decode(encryptedData), key.getBytes(charset), iv.getBytes(charset), false), charset);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AesDecryptFailedException(encryptedData, key, iv, e);
        }
    }

    private byte[] aes(byte[] data, byte[] key, byte[] iv, boolean encryptMode)	throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),
                    new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}