package com.tradin.module.auth.service.dto;

import com.tradin.module.users.domain.UserSocialType;
import com.tradin.module.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDataDto {
    private final String sub;
    private final String email;
    private final String socialId;
    private final UserSocialType socialType;

    public Users toEntity() {
        return Users.builder()
                .sub(sub)
                .email(email)
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }
}
