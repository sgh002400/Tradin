package com.traders.traders.module.users.domain;

import com.traders.traders.common.jpa.AuditTime;
import com.traders.traders.module.strategy.domain.Strategy;
import com.traders.traders.module.strategy.domain.TradingType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

import static com.traders.traders.module.strategy.domain.TradingType.BOTH;
import static com.traders.traders.module.strategy.domain.TradingType.NONE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditTime implements UserDetails {
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
    private int quantityRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType currentPositionType;

    @Column
    private String binanceApiKey;

    @Column
    private String binanceSecretKey;

    @JoinColumn(name = "strategy_id")
    @ManyToOne
    private Strategy strategy;

    @Builder
    private Users(String email, String encryptedPassword) {
        this.name = "트레이더";
        this.email = email;
        this.password = encryptedPassword;
        this.leverage = 1;
        this.quantityRate = 100;
        this.tradingType = BOTH;
        this.currentPositionType = NONE;
        this.binanceApiKey = null;
        this.binanceSecretKey = null;
        this.strategy = null;
    }

    public static Users of(String email, String encryptedPassword) {
        return Users.builder()
                .email(email)
                .encryptedPassword(encryptedPassword)
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
