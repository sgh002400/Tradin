package com.traders.traders.module.users.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;
import static com.traders.traders.module.users.domain.UserSocialType.*;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.common.jwt.JwtProvider;
import com.traders.traders.common.jwt.JwtRemover;
import com.traders.traders.common.utils.SecurityUtils;
import com.traders.traders.module.feign.client.dto.KakaoProfile;
import com.traders.traders.module.feign.service.KakaoFeignService;
import com.traders.traders.module.users.controller.dto.SignInResponseDto;
import com.traders.traders.module.users.controller.dto.response.TokenResponseDto;
import com.traders.traders.module.users.domain.SocialInfo;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.UsersRepository;
import com.traders.traders.module.users.domain.repository.dao.AutoTradingSubscriberDao;
import com.traders.traders.module.users.service.dto.SignUpUserDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
	private final KakaoFeignService kakaoFeignService;
	private final UsersRepository usersRepository;
	private final JwtProvider jwtProvider;
	private final JwtRemover jwtRemover;

	public SignInResponseDto signInWithKakao(String code) {
		KakaoProfile kakaoProfile = kakaoFeignService.getKakaoProfile(code);

		Users user = findUserIdBySocialIdAndSocialType(
			SignUpUserDto.ofKakao(kakaoProfile, KAKAO));

		TokenResponseDto tokenResponseDto = createJwtToken(user.getId());

		return SignInResponseDto.of(user.getId(), tokenResponseDto);
	}

	public SignInResponseDto signInWithGoogle(String code) {
		//TODO

		return null;
	}

	public Users findById(Long id) {
		return usersRepository.findById(id)
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}

	public List<AutoTradingSubscriberDao> findAutoTradingSubscriberByStrategyName(String name) {
		return usersRepository.findByAutoTradingSubscriber(name);
	}

	public Users getUserFromSecurityContext() {
		Long userId = SecurityUtils.getUserId();
		return findById(userId);
	}

	private TokenResponseDto createJwtToken(Long id) {
		return jwtProvider.createJwtToken(id);
	}

	private Users findByEmail(String email) {
		return usersRepository.findByEmail(email)
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}

	private Users findUserIdBySocialIdAndSocialType(SignUpUserDto request) {
		return findUserBySocialInfo(request);
	}

	private Users findUserBySocialInfo(SignUpUserDto request) {
		return usersRepository.findBySocialInfo(SocialInfo.of(request.getSocialId(), request.getSocialType()))
			.orElseGet(() -> signUpUser(request));
	}

	private Users signUpUser(SignUpUserDto request) {
		return usersRepository.save(
			Users.of(request.getName(), request.getEmail(), request.getSocialId(), request.getSocialType()));
	}

	@Override
	public Users loadUserByUsername(String id) {
		return usersRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}
}
