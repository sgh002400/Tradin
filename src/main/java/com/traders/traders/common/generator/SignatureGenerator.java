package com.traders.traders.common.generator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SignatureGenerator {
	public static String generateSignature(String data, String key) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
			byte[] bytes = mac.doFinal(data.getBytes());
			return Hex.encodeHexString(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
