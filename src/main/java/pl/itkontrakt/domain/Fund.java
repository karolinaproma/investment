package pl.itkontrakt.domain;

import lombok.Setter;
import lombok.Builder;
import lombok.Getter;
import pl.itkontrakt.enums.FundType;

import java.math.BigDecimal;

@Getter
@Builder
@Setter
public class Fund {

    private Long id;
    private String name;
    private FundType type;
    private BigDecimal amount;
    private BigDecimal share;
}
