package by.home.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChangeBalanceDto implements Serializable {

    @Pattern(regexp = "^BY13[A-Z]{4}[0-9]{20}$", message = "invalid account number")
    @NotBlank(message = "invalid account number")
    private String account;

    @NotNull(message = "amount must be positive or negative")
    @Digits(integer = 8, fraction = 2,
            message = "the amount must have no more than 8 digits in the integer part and 2 in the fractional")
    private BigDecimal amount;
}
