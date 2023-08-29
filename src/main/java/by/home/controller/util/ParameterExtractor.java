package by.home.controller.util;

import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

import static by.home.util.Constant.ColumnName.ACCOUNT;
import static by.home.util.Constant.ColumnName.ACCOUNT_FROM;
import static by.home.util.Constant.ColumnName.ACCOUNT_TO;
import static by.home.util.Constant.ColumnName.AMOUNT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterExtractor {

    public static ChangeBalanceDto getChangeBalanceDto(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] accounts = parameterMap.get(ACCOUNT);
        String[] amounts = parameterMap.get(AMOUNT);

        if (validate(accounts) && validate(amounts)) {
            return ChangeBalanceDto.builder()
                    .account(accounts[0])
                    .amount(new BigDecimal(amounts[0]))
                    .build();
        } else {
            return ChangeBalanceDto.builder().build();
        }

    }

    public static MoneyTransferDto getMoneyTransferDto(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] accountsFrom = parameterMap.get(ACCOUNT_FROM);
        String[] accountsTo = parameterMap.get(ACCOUNT_TO);
        String[] amounts = parameterMap.get(AMOUNT);

        if (validate(accountsFrom) && validate(accountsTo) && validate(amounts)) {
            return MoneyTransferDto.builder()
                    .accountFrom(accountsFrom[0])
                    .accountTo(accountsTo[0])
                    .amount(new BigDecimal(amounts[0]))
                    .build();
        } else {
            return MoneyTransferDto.builder().build();
        }
    }

    private static boolean validate(String[] params) {
        return params != null && params.length > 0;
    }
}
