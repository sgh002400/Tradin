package com.traders.traders.module.strategy.domain;

import com.traders.traders.common.jpa.AuditTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.traders.traders.module.strategy.domain.TradingType.LONG;
import static com.traders.traders.module.strategy.domain.TradingType.SHORT;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Strategy extends AuditTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StrategyType strategyType;

    @Column(nullable = false)
    private double profitFactor;

    //TODO - Rate, Count 클래스로 묶기

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
    private int totalTradeCount;

    @Column(nullable = false)
    private int winCount;

    @Column(nullable = false)
    private int lossCount;

    @Column(nullable = false)
    private int averageHoldingPeriod;

    @Column(nullable = false)
    private double averageProfitRate;

    @Embedded
    private Position currentPosition;

    @Builder
    private Strategy(String name, StrategyType strategyType, double profitFactor, double winningRate, double simpleProfitRate, double compoundProfitRate,
                     double totalProfitRate, double totalLossRate, int winCount, int lossCount, Position currentPosition, int averageHoldingPeriod, double averageProfitRate) {
        this.name = name;
        this.strategyType = strategyType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.simpleProfitRate = simpleProfitRate;
        this.compoundProfitRate = compoundProfitRate;
        this.totalProfitRate = totalProfitRate;
        this.totalLossRate = totalLossRate;
        this.totalTradeCount = winCount + lossCount;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.currentPosition = currentPosition;
        this.averageHoldingPeriod = averageHoldingPeriod;
        this.averageProfitRate = averageProfitRate;
    }

    public static Strategy of(String name, StrategyType strategyType, double profitFactor, double winningRate, double simpleProfitRate,
                              double compoundProfitRate,
                              double totalProfitRate, double totalLossRate, int winCount, int lossCount, Position currentPosition, int averageHoldingPeriod, double averageProfitRate) {
        return Strategy.builder()
                .name(name)
                .strategyType(strategyType)
                .profitFactor(profitFactor)
                .winningRate(winningRate)
                .simpleProfitRate(simpleProfitRate)
                .compoundProfitRate(compoundProfitRate)
                .totalProfitRate(totalProfitRate)
                .totalLossRate(totalLossRate)
                .winCount(winCount)
                .lossCount(lossCount)
                .currentPosition(currentPosition)
                .averageHoldingPeriod(averageHoldingPeriod)
                .averageProfitRate(averageProfitRate)
                .build();
    }

    public void updateCurrentPosition(Position position) {
        this.currentPosition = position;
    }

    public void updateMetaData(Position position) {
        double profitRate = calculateProfitRate(position);

        if (isWin(profitRate)) {
            addWinCount();
            updateTotalProfitRate(profitRate);
        } else {
            addLossCount();
            updateTotalLossRate(profitRate);
        }

        addTotalTradeCount();
        updateProfitFactor();
        updateWinRate();
        updateSimpleProfitRate();
        updateCompoundProfitRate(profitRate);
        updateCurrentPosition(position);
        updateAverageProfitRate();
    }

    public boolean isLongPosition() {
        return this.currentPosition.getTradingType() == LONG;
    }

    public boolean isShortPosition() {
        return this.currentPosition.getTradingType() == SHORT;
    }

    private void addTotalTradeCount() {
        this.totalTradeCount++;
    }

    private boolean isWin(double profitRate) {
        return profitRate > 0;
    }

    private boolean isCurrentPositionLong() {
        return this.currentPosition.getTradingType() == LONG;
    }

    private void addWinCount() {
        this.winCount++;
    }

    private void addLossCount() {
        this.lossCount++;
    }

    private void updateTotalProfitRate(double profitRate) {
        this.totalProfitRate += profitRate;
    }

    private void updateTotalLossRate(double profitRate) {
        this.totalLossRate -= profitRate;
    }

    private void updateProfitFactor() {
        this.profitFactor = this.totalProfitRate / this.totalLossRate;
    }

    private void updateWinRate() {
        this.winningRate = (double) this.winCount / this.totalTradeCount * 100;
    }

    private void updateSimpleProfitRate() {
        this.simpleProfitRate = this.totalProfitRate - this.totalLossRate;
    }

    private void updateCompoundProfitRate(double profitRate) {
        this.compoundProfitRate = this.compoundProfitRate * (1 + profitRate);
    }

    private void updateAverageProfitRate() {
        this.averageProfitRate = this.simpleProfitRate / this.totalTradeCount;
    }

    private double calculateProfitRate(Position position) {
        if (isCurrentPositionLong()) {
            return ((double) (position.getPrice() - this.currentPosition.getPrice()) / this.currentPosition.getPrice())
                    * 100;
        }

        return ((double) (this.currentPosition.getPrice() - position.getPrice()) / this.currentPosition.getPrice())
                * 100;
    }
}
