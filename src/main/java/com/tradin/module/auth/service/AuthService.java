package com.tradin.module.auth.service;

import com.tradin.common.jwt.JwtUtil;
import com.tradin.module.auth.service.dto.TokenDto;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.feign.service.CognitoFeignService;
import com.tradin.module.users.controller.dto.response.TokenResponseDto;
import com.tradin.module.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tradin.module.users.domain.UserSocialType.GOOGLE;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UsersService usersService;
    private final CognitoFeignService cognitoFeignService;
    private final JwtUtil jwtUtil;

    public void requestTokenToCognito(String code) {
        cognitoFeignService.getTokenFromCognito(code);
    }

    public TokenResponseDto auth(TokenDto request) {
        String idToken = request.getIdToken();
        saveUser(extractUserDataFromIdToken(idToken));

        return TokenResponseDto.of(request.getAccessToken(), request.getRefreshToken());
    }

    private UserDataDto extractUserDataFromIdToken(String idToken) {
        return jwtUtil.extractUserDataFromIdToken(idToken);
    }

    private void saveUser(UserDataDto userDataDto) {
        usersService.saveUser(userDataDto, GOOGLE);
    }

}
