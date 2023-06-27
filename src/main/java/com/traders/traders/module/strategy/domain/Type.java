package com.traders.traders.module.strategy.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Type {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StrategyType strategyType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Builder
    private Type(StrategyType strategyType, CoinType coinType) {
        this.strategyType = strategyType;
        this.coinType = coinType;
    }
}
