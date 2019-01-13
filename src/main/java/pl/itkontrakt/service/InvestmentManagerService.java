package pl.itkontrakt.service;

import pl.itkontrakt.domain.Fund;
import pl.itkontrakt.domain.InvestmentStrategy;
import pl.itkontrakt.enums.FundType;
import pl.itkontrakt.enums.InvestmentStrategyType;
import pl.itkontrakt.exceptions.BusinessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvestmentManagerService {

    public InvestmentStrategy calculateInvestmentStrategy(BigDecimal amount, InvestmentStrategyType strategyType, List<Fund> funds) throws BusinessException {

        Map<FundType, List<Fund>> fundsByType = divideFundsByType(funds);
        List<Fund> amountsInFunds = divideAmount(amount, fundsByType, strategyType);
        BigDecimal amountNotDivided = calculateNotDividedAmount(amount, amountsInFunds);

        InvestmentStrategy investmentStrategy = new InvestmentStrategy();
        investmentStrategy.setInvestmentAmount(amount);
        investmentStrategy.setStrategyType(strategyType);
        investmentStrategy.setFundsWithAmounts(amountsInFunds);
        investmentStrategy.setAmountNotDivided(amountNotDivided);

        return  investmentStrategy;
    }

    private Map<FundType, List<Fund>> divideFundsByType(List<Fund> funds) throws BusinessException {
        if(funds.isEmpty())
            throw new BusinessException("No funds were selected.");

        Map<FundType, List<Fund>> fundsByType = new HashMap<>();
        FundType[] fundTypes = FundType.values();

        for(FundType fundType : fundTypes){
            List<Fund> fundsOfOneType = funds.stream().filter(f -> f.getType().equals(fundType)).collect(Collectors.toList());
            validateDivision(fundType, fundsOfOneType);
            fundsByType.put(fundType,fundsOfOneType);
        }
        return fundsByType;
    }

    private void validateDivision(FundType fundType, List<Fund> fundsOfOneType) throws BusinessException {
        if(fundsOfOneType.isEmpty())
            throw new BusinessException("No fund was selected with type: " + fundType.toString());
    }

    private List<Fund> divideAmount(BigDecimal amount, Map<FundType, List<Fund>> fundsByType, InvestmentStrategyType strategyType){
        List<Fund> amountsInFunds = new ArrayList<>();

        for(Map.Entry<FundType, List<Fund>> fundsOfOneType : fundsByType.entrySet()){
            int listSize = fundsOfOneType.getValue().size();

            BigDecimal percentageShareForThisFundsType = strategyType.getSharesByFundType().get(fundsOfOneType.getKey());
            BigDecimal percentForOneFundOfThisType = percentageShareForThisFundsType.divide(new BigDecimal(listSize), BigDecimal.ROUND_DOWN);
            BigDecimal percentLeft = percentageShareForThisFundsType.subtract(percentForOneFundOfThisType.multiply(new BigDecimal(listSize)));

            List<Fund> fundsListWithShares = allocateShares(fundsOfOneType.getValue(), percentForOneFundOfThisType, percentLeft);
            amountsInFunds.addAll(allocateAmounts(fundsListWithShares, amount));
        }
        return amountsInFunds;
    }

    private List<Fund> allocateShares(List<Fund> fundsList, BigDecimal percentForOneFundOfThisType, BigDecimal percentLeft){
        for(int i = 0; i < fundsList.size(); i++){
            BigDecimal percent = (i==0) ? percentForOneFundOfThisType.add(percentLeft) : percentForOneFundOfThisType;
            Fund singleFund = fundsList.get(i);
            singleFund.setShare(percent);
        }
        return fundsList;
    }

    private List<Fund> allocateAmounts(List<Fund> fundsListWithShares, BigDecimal amount){
        for(Fund singleFund : fundsListWithShares){
            singleFund.setAmount(amount.multiply(singleFund.getShare()).setScale(0, BigDecimal.ROUND_DOWN));
        }
        return fundsListWithShares;
    }

    private BigDecimal calculateNotDividedAmount(BigDecimal amount, List<Fund> amountsInFunds){
        BigDecimal distributedAmount = BigDecimal.ZERO;
        for(Fund amountInFund : amountsInFunds){
            distributedAmount = distributedAmount.add(amountInFund.getAmount());
        }
         return amount.subtract(distributedAmount);
    }
}
