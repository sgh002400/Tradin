package com.traders.traders.module.users.controller.dto.request;

import com.traders.traders.module.users.service.dto.SignUpDto;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "Email must not be blank")
    private String email;

    public SignUpDto toServiceDto() {
        return SignUpDto.of(email);
    }
}
