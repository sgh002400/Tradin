package com.tradin.common.utils;

import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.TradinException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESUtils {
    private static final String ALGORITHM = "AES/CTR/NoPadding";

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_SIZE = 12;
    @Value("${secret.aes-secret}")
    private String key;

    public String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[IV_SIZE];
            random.nextBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);
            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new TradinException(ExceptionMessage.ENCRYPT_FAIL_EXCEPTION);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[IV_SIZE];
            byteBuffer.get(iv);
            byte[] encrypted = new byte[byteBuffer.remaining()];
            byteBuffer.get(encrypted);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
            return new String(cipher.doFinal(encrypted));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new TradinException(ExceptionMessage.DECRYPT_FAIL_EXCEPTION);
        }
    }
}