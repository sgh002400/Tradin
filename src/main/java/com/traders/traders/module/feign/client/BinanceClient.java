package com.traders.traders.module.feign.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.traders.traders.module.feign.client.dto.CurrentPositionInfoDto;

@FeignClient(name = "BinanceClient", url = "https://testnet.binancefuture.com")
public interface BinanceClient {

	@GetMapping("/fapi/v2/positionRisk")
	List<CurrentPositionInfoDto> getCurrentPositionInfo(
		@RequestParam("symbol") String symbol,
		@RequestParam("timestamp") Long timestamp,
		@RequestParam("signature") String signature,
		@RequestHeader("X-MBX-APIKEY") String apiKey);
}
