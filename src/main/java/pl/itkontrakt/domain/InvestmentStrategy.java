package pl.itkontrakt.domain;

import lombok.Getter;
import lombok.Setter;
import pl.itkontrakt.enums.InvestmentStrategyType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class InvestmentStrategy {

    private BigDecimal investmentAmount;
    private InvestmentStrategyType strategyType;
    private List<Fund> fundsWithAmounts;
    private BigDecimal amountNotDivided;
}
