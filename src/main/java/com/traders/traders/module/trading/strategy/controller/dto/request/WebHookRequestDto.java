package com.traders.traders.module.trading.strategy.controller.dto.request;

import com.traders.traders.module.trading.strategy.domain.Position;
import com.traders.traders.module.trading.strategy.service.dto.WebHookDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebHookRequestDto {
	private final String name;
	private final Position position;

	public WebHookDto toServiceDto() {
		return WebHookDto.of(name, position);
	}
}
