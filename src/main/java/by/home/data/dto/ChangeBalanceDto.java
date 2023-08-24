package by.home.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.bval.extras.constraints.checkdigit.IBAN;

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
public class ChangeBalanceDto implements Serializable {

    @IBAN(message = "invalid account number")
    @Pattern(regexp = "^BY13[A-Z]{4}%d{20}$", message = "invalid account number")
    @NotBlank(message = "invalid account number")
    private String account;

    @NotNull(message = "amount must be positive or negative")
    private BigDecimal amount;
}
