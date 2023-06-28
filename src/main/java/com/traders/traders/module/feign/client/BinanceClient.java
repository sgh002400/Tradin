package com.traders.traders.module.feign.client;

import com.traders.traders.module.feign.client.dto.ChangeLeverageDto;
import com.traders.traders.module.feign.client.dto.CurrentPositionInfoDto;
import com.traders.traders.module.feign.client.dto.NewOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "BinanceClient", url = "https://testnet.binancefuture.com")
public interface BinanceClient {

    @GetMapping("/fapi/v2/positionRisk")
    List<CurrentPositionInfoDto> getCurrentPositionInfo(
            @RequestParam("recvWindow") Long recvWindow,
            @RequestParam("symbol") String symbol,
            @RequestParam("timestamp") Long timestamp,
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("signature") String signature);

    @PostMapping("/fapi/v1/order")
    NewOrderDto order(
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("quantity") double quantity,
            @RequestParam("recvWindow") Long recvWindow,
            @RequestParam("side") String side,
            @RequestParam("signature") String signature,
            @RequestParam("symbol") String symbol,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("type") String type
    );

    @PostMapping("/fapi/v1/leverage")
    ChangeLeverageDto changeLeverage(
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("leverage") int leverage,
            @RequestParam("recvWindow") Long recvWindow,
            @RequestParam("symbol") String symbol,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("signature") String signature
    );
}
