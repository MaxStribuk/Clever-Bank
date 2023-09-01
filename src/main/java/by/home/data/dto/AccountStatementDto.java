package by.home.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static by.home.util.Constant.ExceptionMessage.INVALID_ACCOUNT_NUMBER;
import static by.home.util.Constant.ExceptionMessage.INVALID_DATE;
import static by.home.util.Constant.ExceptionMessage.INVALID_DETAILED_PARAMETER;
import static by.home.util.Constant.Utils.ACCOUNT_PATTERN;

/**
 * представляет dto класс для операции генерации выписки
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AccountStatementDto {

    @Pattern(regexp = ACCOUNT_PATTERN, message = INVALID_ACCOUNT_NUMBER)
    @NotBlank(message = INVALID_ACCOUNT_NUMBER)
    private String account;

    @PastOrPresent(message = INVALID_DATE)
    private LocalDate dateFrom;

    @PastOrPresent(message = INVALID_DATE)
    private LocalDate dateTo;

    @NotNull(message = INVALID_DETAILED_PARAMETER)
    private boolean detailed;
}
