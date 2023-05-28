package com.traders.traders.common.generator;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.traders.traders.common.exception.TradersException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SignatureGenerator {
	public String generateSignature(String data, String key) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
			byte[] bytes = mac.doFinal(data.getBytes());
			return Hex.encodeHexString(bytes);
		} catch (Exception e) {
			throw new TradersException(SIGNATURE_GENERATION_FAIL_EXCEPTION);
		}
	}
}
