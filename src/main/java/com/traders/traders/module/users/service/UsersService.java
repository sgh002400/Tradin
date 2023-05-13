package com.traders.traders.module.users.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final JwtRemover jwtRemover;

	public TokenResponseDto signUp(SignUpDto request) {
		checkIfEmailExists(request.getEmail());

		String encryptedPassword = getEncryptedPassword(request.getPassword());
		Users user = saveAndGetUser(request.getEmail(), encryptedPassword);

		return createJwtToken(user.getId());
	}

	public TokenResponseDto signIn(SignInDto request) {
		Users user = findByEmail(request.getEmail());
		checkPasswordCorrespond(request.getPassword(), user.getPassword());

		return createJwtToken(user.getId());
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

	private Users saveAndGetUser(String email, String encryptedPassword) {
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
