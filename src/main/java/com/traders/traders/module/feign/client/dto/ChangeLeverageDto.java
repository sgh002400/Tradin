package com.traders.traders.module.feign.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangeLeverageDto {
    private int leverage;
    private String maxNotionalValue;
    private String symbol;
}
