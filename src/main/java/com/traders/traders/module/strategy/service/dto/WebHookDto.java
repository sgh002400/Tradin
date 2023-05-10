package com.traders.traders.module.strategy.service.dto;

import com.traders.traders.module.strategy.domain.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebHookDto {
	private final String name;
	private final Position position;

	public static WebHookDto of(String name, Position position) {
		return new WebHookDto(name, position);
	}
}
