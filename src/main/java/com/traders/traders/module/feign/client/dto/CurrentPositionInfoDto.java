package com.traders.traders.module.feign.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CurrentPositionInfoDto {
	private String entryPrice;
	private String marginType;
	private String isAutoAddMargin;
	private String isolatedMargin;
	private String leverage;
	private String liquidationPrice;
	private String markPrice;
	private String maxNotionalValue;
	private String positionAmt;
	private String symbol;
	private String unRealizedProfit;
	private String positionSide;
}
