package com.tradin.module.users.domain;

import com.tradin.common.jpa.AuditTime;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

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
    private int quantityRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType currentPositionType;

    @Embedded
    private SocialInfo socialInfo;

    @Column
    private String binanceApiKey;

    @Column
    private String binanceSecretKey;

    @JoinColumn(name = "strategy_id")
    @ManyToOne
    private Strategy strategy;

    @Builder
    private Users(String name, String email, String socialId, UserSocialType socialType) {
        this.name = name;
        this.email = email;
        this.socialInfo = SocialInfo.of(socialId, socialType);
        this.leverage = 1;
        this.quantityRate = 100;
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

    @Builder
    private Users(String email) {
        this.name = "트레이더";
        this.email = email;
        this.leverage = 1;
        this.quantityRate = 100;
        this.tradingType = TradingType.BOTH;
        this.currentPositionType = TradingType.NONE;
        this.binanceApiKey = null;
        this.binanceSecretKey = null;
        this.strategy = null;
    }

    public static Users of(String email) {
        return Users.builder()
                .email(email)
                .build();
    }

    public void subscribeStrategy(Strategy strategy, String encryptedApiKey, String encryptedSecretKey) {
        this.strategy = strategy;
        this.binanceApiKey = encryptedApiKey;
        this.binanceSecretKey = encryptedSecretKey;
    }

    public void changeLeverage(int leverage) {
        this.leverage = leverage;
    }

    public void changeQuantityRate(int quantityRate) {
        this.quantityRate = quantityRate;
    }

    public void changeTradingType(TradingType tradingType) {
        this.tradingType = tradingType;
    }

    public void unsubscribeStrategy() {
        this.strategy = null;
    }

    public void changeCurrentPosition(TradingType tradingType) {
        this.currentPositionType = tradingType;
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
