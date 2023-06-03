package com.traders.traders.module.users.domain;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.traders.traders.common.jpa.AuditTime;
import com.traders.traders.module.strategy.domain.Strategy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditTime implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true)
	private String email;

	@Column(nullable = false)
	private int leverage;

	@Column(nullable = false)
	private double quantity;

	@Column
	private String binanceApiKey;

	@Column
	private String binanceSecretKey;

	@Embedded
	private SocialInfo socialInfo;

	@JoinColumn(name = "strategy_id")
	@ManyToOne
	private Strategy strategy;

	@Builder
	private Users(String name, String email, String socialId, UserSocialType socialType) {
		this.name = name;
		this.email = email;
		this.socialInfo = SocialInfo.of(socialId, socialType);
		this.leverage = 1;
		this.quantity = 0;
		this.binanceApiKey = null;
		this.binanceSecretKey = null;
		this.strategy = null;
	}

	public static Users of(String name, String email, String socialId, UserSocialType socialType) {
		return Users.builder()
			.name(name)
			.email(email)
			.socialId(socialId)
			.socialType(socialType)
			.build();
	}

	public void subscribeStrategy(Strategy strategy, String encryptedApiKey, String encryptedSecretKey) {
		this.strategy = strategy;
		this.binanceApiKey = encryptedApiKey;
		this.binanceSecretKey = encryptedSecretKey;
	}

	public void updateLeverage(int leverage) {
		this.leverage = leverage;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
