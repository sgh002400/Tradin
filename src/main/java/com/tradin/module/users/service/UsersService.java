package com.tradin.module.users.service;

import com.tradin.common.exception.TradinException;
import com.tradin.common.jwt.JwtProvider;
import com.tradin.common.jwt.JwtRemover;
import com.tradin.common.utils.PasswordEncoder;
import com.tradin.common.utils.SecurityUtils;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.feign.service.BinanceFeignService;
import com.tradin.module.users.controller.dto.response.TokenResponseDto;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.domain.repository.UsersRepository;
import com.tradin.module.users.service.dto.ChangeMetadataDto;
import com.tradin.module.users.service.dto.PingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tradin.common.exception.ExceptionMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
    private final BinanceFeignService binanceFeignService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtRemover jwtRemover;



    //TODO - FeignClient 실패 처리하기
    public String ping(PingDto request) {
        binanceFeignService.getBtcusdtPositionQuantity(request.getBinanceApiKey(), request.getBinanceSecretKey());
        return "pong";
    }

    public String changeMetaData(ChangeMetadataDto request) {
        Users user = getUserFromSecurityContext();
        int changedLeverage = getChangedLeverage(request, user);

        user.changeLeverage(changedLeverage);
        user.changeQuantityRate(request.getQuantityRate());
        user.changeTradingType(request.getTradingTypes());

        return "success";
    }

    private int getChangedLeverage(ChangeMetadataDto request, Users user) {
        return binanceFeignService.changeLeverage(user.getBinanceApiKey(), user.getBinanceSecretKey(), request.getLeverage());
    }

    public Users findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    public List<Users> findAutoTradingSubscriberByStrategyName(String name) {
        return usersRepository.findByAutoTradingSubscriber(name);
    }

    public Users getUserFromSecurityContext() {
        Long userId = SecurityUtils.getUserId();
        return findById(userId);
    }

    private void checkIfEmailExists(String email) {
        if (usersRepository.findByEmail(email).isPresent()) {
            throw new TradinException(EMAIL_ALREADY_EXISTS_EXCEPTION);
        }
    }

    private void checkPasswordCorrespond(String password, String encryptedPassword) {
        if (!isPasswordCorrespond(password, encryptedPassword)) {
            throw new TradinException(WRONG_PASSWORD_EXCEPTION);
        }
    }

    private boolean isPasswordCorrespond(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }

    private Users saveUser(UserDataDto userDataDto) {
        Users users = userDataDto.toEntity();
        return usersRepository.save(users);
    }

    private static Users createUser(String email) {
        return Users.builder()
                .email(email)
                .build();
    }

    private String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private TokenResponseDto createJwtToken(Long id) {
        return jwtProvider.createJwtToken(id);
    }

    private Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    @Override
    public Users loadUserByUsername(String id) {
        return usersRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }
}
