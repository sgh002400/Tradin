package com.traders.traders.module.user;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.traders.traders.common.jpa.AuditTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private int leverage;

	@Column(nullable = false)
	private long quantity;

	@Embedded
	private SocialInfo socialInfo;

	@Builder
	private Users(String name, String email, String password, SocialInfo socialInfo) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.leverage = 1;
		this.quantity = 0;
		this.socialInfo = socialInfo;
	}

	public static Users of(String name, String email, String password, String socialId, SocialType socialType) {
		return Users.builder()
			.name(name)
			.email(email)
			.password(password)
			.socialInfo(SocialInfo.of(socialId, socialType))
			.build();
	}
}
