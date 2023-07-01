package com.traders.traders.module.strategy.controller.dto.request;

import com.traders.traders.module.strategy.domain.Position;
import com.traders.traders.module.strategy.service.dto.WebHookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WebHookRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private Position position;

    public WebHookDto toServiceDto() {
        return WebHookDto.of(name, position);
    }
}
