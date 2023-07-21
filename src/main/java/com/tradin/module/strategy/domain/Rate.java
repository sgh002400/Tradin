package com.tradin.module.strategy.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Rate {
    @Column(nullable = false)
    private double winningRate;

    @Column(nullable = false)
    private double simpleProfitRate; //단리 수익률

    @Column(nullable = false)
    private double compoundProfitRate; // 복리 수익률

    @Column(nullable = false)
    private double totalProfitRate; //총 수익률

    @Column(nullable = false)
    private double totalLossRate; //총 손해율

    @Column(nullable = false)
    private double averageProfitRate;

    @Builder
    private Rate(double winningRate, double simpleProfitRate, double compoundProfitRate, double totalProfitRate, double totalLossRate, double averageProfitRate) {
        this.winningRate = winningRate;
        this.simpleProfitRate = simpleProfitRate;
        this.compoundProfitRate = compoundProfitRate;
        this.totalProfitRate = totalProfitRate;
        this.totalLossRate = totalLossRate;
        this.averageProfitRate = averageProfitRate;
    }

    public void updateTotalProfitRate(double profitRate) {
        this.totalProfitRate += profitRate;
    }

    public void updateTotalLossRate(double lossRate) {
        this.totalLossRate -= lossRate;
    }

    public void updateWinRate(int winCount, int totalTradeCount) {
        this.winningRate = (double) winCount / totalTradeCount * 100;
    }

    public void updateSimpleProfitRate() {
        this.simpleProfitRate = this.totalProfitRate - this.totalLossRate;
    }

    public void updateCompoundProfitRate(double profitRate) {
        this.compoundProfitRate = ((1 + this.compoundProfitRate / 100.0) * (1 + profitRate / 100.0)) - 1;
        this.compoundProfitRate *= 100;
    }

    public void updateAverageProfitRate(int totalTradeCount) {
        this.averageProfitRate = this.simpleProfitRate / totalTradeCount;
    }

}
