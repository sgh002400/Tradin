package com.traders.traders.common.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESUtils {
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	@Value("${secret.aes-secret}")
	private String key;

	public String encrypt(String text) throws Exception {
		String iv = key.substring(0, 16);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		byte[] encrypted = cipher.doFinal(text.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public String decrypt(String encryptedText) throws Exception {
		String iv = key.substring(0, 16);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] decoded = Base64.getDecoder().decode(encryptedText);
		return new String(cipher.doFinal(decoded));
	}
}