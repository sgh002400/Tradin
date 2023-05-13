package com.traders.traders.module.users.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.common.jwt.JwtProvider;
import com.traders.traders.common.jwt.JwtRemover;
import com.traders.traders.common.utils.PasswordEncoder;
import com.traders.traders.module.users.controller.dto.response.TokenResponseDto;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.UsersRepository;
import com.traders.traders.module.users.service.dto.SignInDto;
import com.traders.traders.module.users.service.dto.SignUpDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final JwtRemover jwtRemover;

	public TokenResponseDto signUp(SignUpDto request) {
		String encryptedPassword = getEncryptedPassword(request.getPassword());
		Users user = saveAndGetUser(request.getEmail(), encryptedPassword);

		return createJwtToken(user.getId());
	}

	public TokenResponseDto signIn(SignInDto request) {
		Users user = findByEmail(request);
		checkPasswordCorrespond(request.getPassword(), user.getPassword());

		return createJwtToken(user.getId());
	}

	private void checkPasswordCorrespond(String password, String encryptedPassword) {
		if (!isPasswordCorrespond(password, encryptedPassword)) {
			throw new TradersException(WRONG_PASSWORD_EXCEPTION);
		}
	}

	private boolean isPasswordCorrespond(String password, String encryptedPassword) {
		return passwordEncoder.matches(password, encryptedPassword);
	}

	private Users saveAndGetUser(String email, String encryptedPassword) {
		//TODO - 이메일 동일할 때 어떤 예외 발생하는지 테스트
		Users users = createUser(email, encryptedPassword);
		return usersRepository.save(users);
	}

	private static Users createUser(String email, String encryptedPassword) {
		return Users.builder()
			.email(email)
			.password(encryptedPassword)
			.build();
	}

	private String getEncryptedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	private TokenResponseDto createJwtToken(Long id) {
		return jwtProvider.createJwtToken(id);
	}

	private Users findByEmail(SignInDto request) {
		return usersRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}

	@Override
	public Users loadUserByUsername(String id) {
		return usersRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}
}
