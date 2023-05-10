package com.traders.traders.module.feign.service;

import java.time.Instant;
import java.util.List;

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
	private final BinanceClient feignClient;

	public List<CurrentPositionInfoDto> getCurrentPositionInfo(String apiKey, String secretKey) {
		Long timestamp = Instant.now().toEpochMilli();
		String queryString = "symbol=" + "BTCUSDT" + "&timestamp=" + timestamp;
		String signature = SignatureGenerator.generateSignature(queryString, secretKey);

		return feignClient.getCurrentPositionInfo("BTCUSDT", timestamp, apiKey, signature);
	}

}
