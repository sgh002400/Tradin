package com.traders.traders.module.feign.service;

import com.traders.traders.common.secret.SecretKeyManager;
import com.traders.traders.module.feign.client.GoogleAuthorizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleFeignService {
    private final SecretKeyManager secretKeyManager;
    private final GoogleAuthorizationClient googleAuthorizationClient;

}
