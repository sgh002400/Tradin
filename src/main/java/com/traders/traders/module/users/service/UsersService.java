package com.traders.traders.module.users.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.common.jwt.JwtProvider;
import com.traders.traders.common.jwt.JwtRemover;
import com.traders.traders.common.jwt.JwtUtil;
import com.traders.traders.common.utils.PasswordEncoderUtils;
import com.traders.traders.module.users.controller.dto.response.TokenResponseDto;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.UsersRepository;
import com.traders.traders.module.users.service.dto.SignUpDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
	private final UsersRepository usersRepository;
	private final PasswordEncoderUtils passwordEncoderUtils;
	private final JwtUtil jwtUtil;
	private final JwtProvider jwtProvider;
	private final JwtRemover jwtRemover;

	public TokenResponseDto signup(SignUpDto request) {
		String encryptedPassword = getEncryptedPassword(request);
		Users user = saveAndGetUser(request.getEmail(), encryptedPassword);

		return createJwtToken(user.getId());
	}

	private Users saveAndGetUser(String email, String encryptedPassword) {
		Users users = Users.builder()
			.email(email)
			.password(encryptedPassword)
			.build();

		return usersRepository.save(users);
	}

	private String getEncryptedPassword(SignUpDto request) {
		return passwordEncoderUtils.encode(request.getPassword());
	}

	private TokenResponseDto createJwtToken(Long id) {
		return jwtProvider.createJwtToken(id);
	}

	@Override
	public Users loadUserByUsername(String id) {
		return usersRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}
}
