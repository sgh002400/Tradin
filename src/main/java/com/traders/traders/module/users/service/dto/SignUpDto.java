package com.traders.traders.module.users.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignUpDto {
    private String email;

    public static SignUpDto of(String email) {
        return new SignUpDto(email);
    }
}
