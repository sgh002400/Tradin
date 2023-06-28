package com.traders.traders.module.feign.service;

import com.traders.traders.common.generator.SignatureGenerator;
import com.traders.traders.module.feign.client.BinanceClient;
import com.traders.traders.module.feign.client.dto.CurrentPositionInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeignService {
    private static final String TICKER = "BTCUSDT";
    private final BinanceClient feignClient;

    //queryString은 이름 기준으로 오름차순으로 정렬해야 됨. 요청을 보낼 때 파라미터 순서는 queryString과 동일해야 됨
    public void switchPosition(String apiKey, String secretKey, String side, double orderQuantity) {
        Long timestamp = Instant.now().toEpochMilli();
        double quantity = Math.abs(getBtcusdtPositionQuantity(apiKey, secretKey)) + orderQuantity;

        String queryString =
                "quantity=" + quantity + "&recvWindow=1000" + "&side=" + side + "&symbol=" + TICKER + "&timestamp="
                        + timestamp
                        + "&type=MARKET";

        String signature = SignatureGenerator.generateSignature(queryString, secretKey);

        feignClient.closePosition(apiKey, quantity, 1000L, side, signature, TICKER, timestamp, "MARKET");
    }

    public Double getBtcusdtPositionQuantity(String apiKey, String secretKey) {
        Long timestamp = Instant.now().toEpochMilli();
        String queryString = "recvWindow=1000" + "&symbol=" + TICKER + "&timestamp=" + timestamp;
        String signature = SignatureGenerator.generateSignature(queryString, secretKey);

        List<CurrentPositionInfoDto> currentPositionInfoDtos = feignClient.getCurrentPositionInfo(1000L, TICKER,
                timestamp,
                apiKey, signature);

        return extractBtcusdtPositionQuantity(currentPositionInfoDtos);
    }

    private Double extractBtcusdtPositionQuantity(List<CurrentPositionInfoDto> currentPositionInfoDtos) {
        for (CurrentPositionInfoDto dto : currentPositionInfoDtos) {
            if (dto.getSymbol().equals(TICKER)) {
                return Double.parseDouble(dto.getPositionAmt());
            }
        }
        return null;
    }
}
