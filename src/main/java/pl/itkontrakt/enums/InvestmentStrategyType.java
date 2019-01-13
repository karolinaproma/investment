package pl.itkontrakt.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum InvestmentStrategyType {

    SAFE(0.2, 0.75, 0.05),
    BALANCED(0.3, 0.6, 0.1),
    AGGRESSIVE(0.4, 0.2, 0.4);

    private Map<FundType, BigDecimal> sharesByFundType;

    InvestmentStrategyType(double polishShare, double foreginShare, double moneyShare){
        sharesByFundType = new HashMap<>();
        sharesByFundType.put(FundType.POLISH, new BigDecimal(polishShare).setScale(4, BigDecimal.ROUND_DOWN));
        sharesByFundType.put(FundType.FOREIGN, new BigDecimal(foreginShare).setScale(4, BigDecimal.ROUND_DOWN));
        sharesByFundType.put(FundType.MONEY, new BigDecimal(moneyShare).setScale(4, BigDecimal.ROUND_DOWN));
    }
}
