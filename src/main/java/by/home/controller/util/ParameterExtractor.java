package by.home.controller.util;

import by.home.data.dto.AccountStatementDto;
import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static by.home.util.Constant.ColumnName.ACCOUNT;
import static by.home.util.Constant.ColumnName.ACCOUNT_FROM;
import static by.home.util.Constant.ColumnName.ACCOUNT_TO;
import static by.home.util.Constant.ColumnName.AMOUNT;
import static by.home.util.Constant.Utils.DATE_FROM;
import static by.home.util.Constant.Utils.DATE_TO;
import static by.home.util.Constant.Utils.DETAILED_FLAG;

/**
 * Данный утилитный класс осуществляет парсинг параметров запроса и
 * создание объектов dto классов.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterExtractor {

    public static ChangeBalanceDto getChangeBalanceDto(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] accounts = parameterMap.get(ACCOUNT);
        String[] amounts = parameterMap.get(AMOUNT);
        ChangeBalanceDto changeBalanceDto = ChangeBalanceDto.builder().build();

        if (validate(accounts)) {
            changeBalanceDto.setAccount(accounts[0]);
        }
        if (validate(amounts)) {
            try {
                changeBalanceDto.setAmount(new BigDecimal(amounts[0]));
            } catch (NumberFormatException e) {
                changeBalanceDto.setAmount(null);
            }
        }
        return changeBalanceDto;
    }

    public static MoneyTransferDto getMoneyTransferDto(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] accountsFrom = parameterMap.get(ACCOUNT_FROM);
        String[] accountsTo = parameterMap.get(ACCOUNT_TO);
        String[] amounts = parameterMap.get(AMOUNT);
        MoneyTransferDto moneyTransferDto = MoneyTransferDto.builder().build();

        if (validate(accountsFrom)) {
            moneyTransferDto.setAccountFrom(accountsFrom[0]);
        }
        if (validate(accountsTo)) {
            moneyTransferDto.setAccountTo(accountsTo[0]);
        }
        if (validate(amounts)) {
            try {
                moneyTransferDto.setAmount(new BigDecimal(amounts[0]));
            } catch (NumberFormatException e) {
                moneyTransferDto.setAmount(null);
            }
        }
        return moneyTransferDto;
    }

    public static AccountStatementDto getAccountStatementDto(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] accounts = parameterMap.get(ACCOUNT);
        String[] datesFrom = parameterMap.get(DATE_FROM);
        String[] datesTo = parameterMap.get(DATE_TO);
        String[] details = parameterMap.get(DETAILED_FLAG);
        AccountStatementDto accountStatementDto = AccountStatementDto.builder().build();

        if (validate(accounts)) {
            accountStatementDto.setAccount(accounts[0]);
        }
        if (validate(details)) {
            accountStatementDto.setDetailed(Boolean.parseBoolean(details[0]));
        }
        if (validate(datesFrom)) {
            accountStatementDto.setDateFrom(getValidDate(datesFrom[0]));
        }
        if (validate(datesTo)) {
            accountStatementDto.setDateFrom(getValidDate(datesTo[0]));
        }
        return accountStatementDto;
    }

    private static LocalDate getValidDate(String s) {
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static boolean validate(String[] params) {
        return params != null && params.length > 0;
    }
}
