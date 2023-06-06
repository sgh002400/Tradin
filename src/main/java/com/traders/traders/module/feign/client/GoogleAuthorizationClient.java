package com.traders.traders.module.feign.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "googleAuthorizationClient", url = "https://.com")

public interface GoogleAuthorizationClient {
}
