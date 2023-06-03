package com.traders.traders.module.feign.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoProfile {
	private String id;
	private KakaoAccount kakaoAccount;

	@Getter
	public static class KakaoAccount {
		private Profile profile;
		private String email;
	}

	@Getter
	public static class Profile {
		private String nickname;
	}
}


