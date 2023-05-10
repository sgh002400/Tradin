package com.traders.traders.module.strategy.controller.dto.request;

import javax.validation.constraints.NotBlank;

import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.service.dto.WebHookDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WebHookRequestDto {
	@NotBlank
	private String name;

	@NotBlank //TODO - test
	private Position position;

	public WebHookDto toServiceDto() {
		return WebHookDto.of(name, position);
	}
}
