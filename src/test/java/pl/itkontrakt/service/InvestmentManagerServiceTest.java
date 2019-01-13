package pl.itkontrakt.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.itkontrakt.domain.Fund;
import pl.itkontrakt.domain.InvestmentStrategy;
import pl.itkontrakt.enums.FundType;
import pl.itkontrakt.enums.InvestmentStrategyType;
import pl.itkontrakt.exceptions.BusinessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvestmentManagerServiceTest {

    private InvestmentManagerService serviceUnderTests = new InvestmentManagerService();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowBusinessException() throws BusinessException {
        thrown.expect(BusinessException.class);
        serviceUnderTests.calculateInvestmentStrategy(new BigDecimal(10000), InvestmentStrategyType.SAFE, prepareFundsForExceptionCase());
    }

    private List<Fund> prepareFundsForExceptionCase(){
        List<Fund> funds = new ArrayList<>();
        funds.add(Fund.builder().id(1L).name("Fundusz Polski 1").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(2L).name("Fundusz Polski 2").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(3L).name("Fundusz Zagraniczny 1").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(4L).name("Fundusz Zagraniczny 2").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(5L).name("Fundusz Zagraniczny 3").type(FundType.FOREIGN).build());
        return  funds;
    }

    @Test
    public void shouldCalculateInvestmentStrategyCase1() throws BusinessException {

        InvestmentStrategy calculatedInvestmentStrategy = serviceUnderTests.calculateInvestmentStrategy(new BigDecimal(10001), InvestmentStrategyType.SAFE, prepareFundsForCase1());

        assertThat(calculatedInvestmentStrategy.getFundsWithAmounts(), hasSize(6));

        Optional<Fund> fund1 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(1L)).findAny();
        Optional<Fund> fund2 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(2L)).findAny();
        Optional<Fund> fund3 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(3L)).findAny();
        Optional<Fund> fund4 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(4L)).findAny();
        Optional<Fund> fund5 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(5L)).findAny();
        Optional<Fund> fund6 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(6L)).findAny();

        Assert.assertEquals(new BigDecimal(1000), fund1.get().getAmount());
        Assert.assertEquals(new BigDecimal(1000), fund2.get().getAmount());
        Assert.assertEquals(new BigDecimal(2500), fund3.get().getAmount());
        Assert.assertEquals(new BigDecimal(2500), fund4.get().getAmount());
        Assert.assertEquals(new BigDecimal(2500), fund5.get().getAmount());
        Assert.assertEquals(new BigDecimal(500), fund6.get().getAmount());
        Assert.assertEquals(new BigDecimal(1), calculatedInvestmentStrategy.getAmountNotDivided());
    }

    private List<Fund> prepareFundsForCase1(){
        List<Fund> funds = new ArrayList<>();
        funds.add(Fund.builder().id(1L).name("Fundusz Polski 1").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(2L).name("Fundusz Polski 2").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(3L).name("Fundusz Zagraniczny 1").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(4L).name("Fundusz Zagraniczny 2").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(5L).name("Fundusz Zagraniczny 3").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(6L).name("Fundusz Pieniężny 1").type(FundType.MONEY).build());
        return  funds;
    }

    @Test
    public void shouldCalculateInvestmentStrategyCase2() throws BusinessException {

        InvestmentStrategy calculatedInvestmentStrategy = serviceUnderTests.calculateInvestmentStrategy(new BigDecimal(10000), InvestmentStrategyType.SAFE, prepareFundsForCase2());

        assertThat(calculatedInvestmentStrategy.getFundsWithAmounts(), hasSize(6));

        Optional<Fund> fund1 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(1L)).findAny();
        Optional<Fund> fund2 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(2L)).findAny();
        Optional<Fund> fund3 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(3L)).findAny();
        Optional<Fund> fund4 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(4L)).findAny();
        Optional<Fund> fund5 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(5L)).findAny();
        Optional<Fund> fund6 = calculatedInvestmentStrategy.getFundsWithAmounts().stream().filter(f -> f.getId().equals(6L)).findAny();

        Assert.assertEquals(new BigDecimal(668), fund1.get().getAmount());
        Assert.assertEquals(new BigDecimal(666), fund2.get().getAmount());
        Assert.assertEquals(new BigDecimal(666), fund3.get().getAmount());
        Assert.assertEquals(new BigDecimal(3750), fund4.get().getAmount());
        Assert.assertEquals(new BigDecimal(3750), fund5.get().getAmount());
        Assert.assertEquals(new BigDecimal(500), fund6.get().getAmount());
        Assert.assertEquals(new BigDecimal(0), calculatedInvestmentStrategy.getAmountNotDivided());
    }

    private List<Fund> prepareFundsForCase2(){
        List<Fund> funds = new ArrayList<>();
        funds.add(Fund.builder().id(1L).name("Fundusz Polski 1").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(2L).name("Fundusz Polski 2").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(3L).name("Fundusz Polski 3").type(FundType.POLISH).build());
        funds.add(Fund.builder().id(4L).name("Fundusz Zagraniczny 2").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(5L).name("Fundusz Zagraniczny 3").type(FundType.FOREIGN).build());
        funds.add(Fund.builder().id(6L).name("Fundusz Pieniężny 1").type(FundType.MONEY).build());
        return  funds;
    }
}
