package com.traders.traders.common.utils;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.traders.traders.common.exception.TradersException;

@Component
public class AESUtils {
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	@Value("${secret.aes-secret}")
	private String key;

	public String encrypt(String text) {
		try {
			String iv = key.substring(0, 16);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			byte[] encrypted = cipher.doFinal(text.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
				 InvalidAlgorithmParameterException | IllegalBlockSizeException |
				 BadPaddingException e) {
			throw new TradersException(ENCRYPT_FAIL_EXCEPTION);
		}
	}

	public String decrypt(String encryptedText) {
		try {
			String iv = key.substring(0, 16);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			byte[] decoded = Base64.getDecoder().decode(encryptedText);
			return new String(cipher.doFinal(decoded));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
				 InvalidAlgorithmParameterException |
				 IllegalBlockSizeException | BadPaddingException e) {
			throw new TradersException(DECRYPT_FAIL_EXCEPTION);
		}
	}
}
