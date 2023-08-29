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
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

import static by.home.util.Constant.ExceptionMessage.ACCOUNT_MUST_BE_POSITIVE;
import static by.home.util.Constant.ExceptionMessage.INVALID_ACCOUNT_FROM_NUMBER;
import static by.home.util.Constant.ExceptionMessage.INVALID_ACCOUNT_NUMBER;
import static by.home.util.Constant.ExceptionMessage.INVALID_ACCOUNT_TO_NUMBER;
import static by.home.util.Constant.ExceptionMessage.INVALID_DIGITS;
import static by.home.util.Constant.Utils.ACCOUNT_PATTERN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MoneyTransferDto implements Serializable {

    @Pattern(regexp = ACCOUNT_PATTERN, message = INVALID_ACCOUNT_NUMBER)
    @NotBlank(message = INVALID_ACCOUNT_FROM_NUMBER)
    private String accountFrom;

    @Pattern(regexp = ACCOUNT_PATTERN, message = INVALID_ACCOUNT_NUMBER)
    @NotBlank(message = INVALID_ACCOUNT_TO_NUMBER)
    private String accountTo;

    @NotNull(message = ACCOUNT_MUST_BE_POSITIVE)
    @Digits(integer = 8, fraction = 2, message = INVALID_DIGITS)
    @Positive(message = ACCOUNT_MUST_BE_POSITIVE)
    private BigDecimal amount;
}
