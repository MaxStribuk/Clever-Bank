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

import static by.home.util.Constant.ExceptionMessage.AMOUNT_MUST_BE_POSITIVE_OR_NEGATIVE;
import static by.home.util.Constant.ExceptionMessage.INVALID_ACCOUNT_NUMBER;
import static by.home.util.Constant.ExceptionMessage.INVALID_DIGITS;
import static by.home.util.Constant.Utils.ACCOUNT_PATTERN;

/**
 * представляет dto класс для операции изменения баланса
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChangeBalanceDto implements Serializable {

    @Pattern(regexp = ACCOUNT_PATTERN, message = INVALID_ACCOUNT_NUMBER)
    @NotBlank(message = INVALID_ACCOUNT_NUMBER)
    private String account;

    @NotNull(message = AMOUNT_MUST_BE_POSITIVE_OR_NEGATIVE)
    @Digits(integer = 8, fraction = 2, message = INVALID_DIGITS)
    private BigDecimal amount;
}
