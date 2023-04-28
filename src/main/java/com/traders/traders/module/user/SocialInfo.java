package com.traders.traders.module.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SocialInfo {
	@Column(nullable = false, length = 200)
	private String socialId;

	@Column(nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	private SocialInfo(String socialId, SocialType socialType) {
		this.socialId = socialId;
		this.socialType = socialType;
	}

	public static SocialInfo of(String socialId, SocialType socialType) {
		return new SocialInfo(socialId, socialType);
	}
}
