package com.traders.traders.module.users.service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.common.jwt.JwtProvider;
import com.traders.traders.common.jwt.JwtRemover;
import com.traders.traders.common.utils.PasswordEncoder;
import com.traders.traders.common.utils.SecurityUtils;
import com.traders.traders.module.feign.service.FeignService;
import com.traders.traders.module.users.controller.dto.response.TokenResponseDto;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.UsersRepository;
import com.traders.traders.module.users.service.dto.ChangeMetadataDto;
import com.traders.traders.module.users.service.dto.PingDto;
import com.traders.traders.module.users.service.dto.SignInDto;
import com.traders.traders.module.users.service.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.traders.traders.common.exception.ExceptionMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
    private final FeignService feignService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtRemover jwtRemover;

    public TokenResponseDto signUp(SignUpDto request) {
        checkIfEmailExists(request.getEmail());
        Users user = saveUser(request.getEmail());

        return createJwtToken(user.getId());
    }

    public TokenResponseDto signIn(SignInDto request) {
        Users user = findByEmail(request.getEmail());
        checkPasswordCorrespond(request.getPassword(), user.getPassword());

        return createJwtToken(user.getId());
    }

    //TODO - FeignClient 실패 처리하기
    public String ping(PingDto request) {
        feignService.getBtcusdtPositionQuantity(request.getBinanceApiKey(), request.getBinanceSecretKey());
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
        return feignService.changeLeverage(user.getBinanceApiKey(), user.getBinanceSecretKey(), request.getLeverage());
    }

    public Users findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
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
            throw new TradersException(EMAIL_ALREADY_EXISTS_EXCEPTION);
        }
    }

    private void checkPasswordCorrespond(String password, String encryptedPassword) {
        if (!isPasswordCorrespond(password, encryptedPassword)) {
            throw new TradersException(WRONG_PASSWORD_EXCEPTION);
        }
    }

    private boolean isPasswordCorrespond(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }

    private Users saveUser(String email) {
        Users users = createUser(email);
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
                .orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
    }

    @Override
    public Users loadUserByUsername(String id) {
        return usersRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
    }
}
