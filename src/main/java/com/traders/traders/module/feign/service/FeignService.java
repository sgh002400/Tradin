package com.traders.traders.module.feign.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.generator.SignatureGenerator;
import com.traders.traders.module.feign.client.BinanceClient;
import com.traders.traders.module.feign.client.dto.CurrentPositionInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FeignService {
	private static final String TICKER = "BTCUSDT";
	private final BinanceClient feignClient;

	public CurrentPositionInfoDto getCurrentPositionInfo(String apiKey, String secretKey) {
		Long timestamp = Instant.now().toEpochMilli();
		String queryString = "symbol=" + TICKER + "&timestamp=" + timestamp;
		String signature = SignatureGenerator.generateSignature(queryString, secretKey);

		return feignClient.getCurrentPositionInfo(TICKER, timestamp, apiKey, signature).get(0);
	}

	// public void closePosition(String apiKey, String secretKey) {
	//closePosition = true 넣어서 요청 보내기
	// 	Long timestamp = Instant.now().toEpochMilli();
	// 	String queryString = "symbol=" + TICKER + "&timestamp=" + timestamp;
	// 	String signature = SignatureGenerator.generateSignature(queryString, secretKey);
	//
	// 	feignClient.closePosition(TICKER, timestamp, apiKey, signature);
	// }

	public void createOrder(String apiKey, String secretKey, String side, String quantity) {
		Long timestamp = Instant.now().toEpochMilli();
		String queryString = "symbol=" + TICKER + "&side=BUY&type=MARKET&quantity=1" + "&timestamp=" + timestamp;
		String signature = SignatureGenerator.generateSignature(queryString, secretKey);

		feignClient.createOrder(TICKER, "BUY", "MARKET", "1", timestamp, apiKey, signature);
	}
}
