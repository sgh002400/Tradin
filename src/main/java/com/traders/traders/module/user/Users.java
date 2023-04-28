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

	@Column(unique = true)
	private String email;

	@Embedded
	private SocialInfo socialInfo;

	@Builder
	private Users(String name, String email, SocialInfo socialInfo) {
		this.name = name;
		this.email = email;
		this.socialInfo = socialInfo;
	}

	public static Users of(String name, String email, String socialId, SocialType socialType) {
		return Users.builder()
			.name(name)
			.email(email)
			.socialInfo(SocialInfo.of(socialId, socialType))
			.build();
	}
}
