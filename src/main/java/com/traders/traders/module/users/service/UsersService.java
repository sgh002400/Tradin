package com.traders.traders.module.users.service;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.traders.traders.common.exception.TradersException;
import com.traders.traders.module.users.domain.Users;
import com.traders.traders.module.users.domain.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
	private final UsersRepository usersRepository;

	@Override
	public Users loadUserByUsername(String id) {
		return usersRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new TradersException(NOT_FOUND_USER_EXCEPTION));
	}
}
