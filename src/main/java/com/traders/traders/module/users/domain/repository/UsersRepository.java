package com.traders.traders.module.users.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traders.traders.module.users.domain.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, UsersQueryRepository {
	Optional<Users> findByEmail(String email);
}
