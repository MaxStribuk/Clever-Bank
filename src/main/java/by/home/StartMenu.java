package by.home;

import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.ErrorDto;
import by.home.data.dto.MoneyTransferDto;
import by.home.data.exception.InvalidArgsException;
import by.home.factory.service.AccountServiceSingleton;
import by.home.factory.util.ConnectionSingleton;
import by.home.factory.util.ValidationManagerSingleton;
import by.home.service.api.IAccountService;
import by.home.service.util.ConstraintViolationUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class StartMenu {

    private final IAccountService accountService;

    public StartMenu() {
        accountService = AccountServiceSingleton.getInstance();
    }

    public void start(String[] args) {
        try {
            chooseOperation(args);
            System.out.println("Operation successful complete");
        } catch (NumberFormatException e) {
            log.error("invalid BigDecimal");
        } catch (ConstraintViolationException e) {
            List<ErrorDto> errors = ConstraintViolationUtil.getErrors(e.getConstraintViolations());
            log.error(errors.toString());
        } catch (Throwable e) {
            log.error(e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void chooseOperation(String[] args) {
        switch (args.length) {
            case 2 -> changeBalance(args);
            case 3 -> transferMoney(args);
            default -> throw new InvalidArgsException("invalid args length");
        }
    }

    private void transferMoney(String[] args) {
        accountService.transferMoney(
                new MoneyTransferDto(args[0], args[1], new BigDecimal(args[2]))
        );
    }

    private void changeBalance(String[] args) {
        accountService.changeBalance(
                new ChangeBalanceDto(args[0], new BigDecimal(args[1]))
        );
    }

    private void closeResources() {
        try {
            ValidationManagerSingleton.getInstance().close();
            ConnectionSingleton.getInstance().close();
        } catch (Exception e) {
            log.error("resources cannot close");
        }
    }
}
