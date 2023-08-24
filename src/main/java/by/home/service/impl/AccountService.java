package by.home.service.impl;

import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
import by.home.dao.entity.Transaction;
import by.home.dao.entity.TransactionType;
import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;
import by.home.data.exception.AccountNotFoundException;
import by.home.data.exception.InvalidArgsException;
import by.home.data.exception.InvalidBalanceException;
import by.home.service.api.IAccountService;
import by.home.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class AccountService implements IAccountService {

    private final IAccountDao accountDao;
    private final Validator validator;
    private final ITransactionService transactionService;

    @Override
    public void changeBalance(ChangeBalanceDto changeBalanceDto) {
        validate(changeBalanceDto);
        BigDecimal amount = changeBalanceDto.getAmount();
        Account account = accountDao.findById(changeBalanceDto.getAccount())
                .orElseThrow(() -> new AccountNotFoundException("account not found"));
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            writeOffMoney(account, amount);
        } else {
            addMoney(account, amount);
        }
        transactionService.add(getTransaction(changeBalanceDto));
    }

    @Override
    public void transferMoney(MoneyTransferDto moneyTransferDto) {
        validate(moneyTransferDto);
        BigDecimal amount = moneyTransferDto.getAmount();
        Account accountFrom = accountDao.findById(moneyTransferDto.getAccountFrom())
                .orElseThrow(() -> new AccountNotFoundException("account not found"));
        Account accountTo = accountDao.findById(moneyTransferDto.getAccountTo())
                .orElseThrow(() -> new AccountNotFoundException("account not found"));
        writeOffMoney(accountFrom, amount);
        addMoney(accountTo, amount);
        transactionService.add(getTransaction(moneyTransferDto));
    }

    private void writeOffMoney(Account account, BigDecimal amount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("not enough money on balance");
        }
        account.setBalance(newBalance);
        accountDao.update(account);
    }

    private void addMoney(Account account, BigDecimal amount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        account.setBalance(newBalance);
        accountDao.update(account);
    }

    private void validate(ChangeBalanceDto changeBalanceDto) {
        Set<ConstraintViolation<ChangeBalanceDto>> violations = validator.validate(changeBalanceDto);
        BigDecimal amount = changeBalanceDto.getAmount();
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        validateAmount(amount);
    }

    private void validate(MoneyTransferDto moneyTransferDto) {
        Set<ConstraintViolation<MoneyTransferDto>> violations = validator.validate(moneyTransferDto);
        BigDecimal amount = moneyTransferDto.getAmount();
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        validateAmount(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidArgsException("amount must be positive or negative");
        }
    }

    private Transaction getTransaction(ChangeBalanceDto changeBalanceDto) {
        BigDecimal amount = changeBalanceDto.getAmount();
        TransactionType type = amount.compareTo(BigDecimal.ZERO) > 0
                ? TransactionType.REFILL
                : TransactionType.WITHDRAWAL;
        String account = changeBalanceDto.getAccount();
        return Transaction.builder()
                .accountTo(account)
                .accountFrom(account)
                .time(LocalDateTime.now())
                .id(UUID.randomUUID())
                .typeId(TransactionType.getId(type))
                .amount(amount)
                .build();
    }

    private Transaction getTransaction(MoneyTransferDto moneyTransferDto) {
        return Transaction.builder()
                .accountTo(moneyTransferDto.getAccountTo())
                .accountFrom(moneyTransferDto.getAccountFrom())
                .time(LocalDateTime.now())
                .id(UUID.randomUUID())
                .typeId(TransactionType.getId(TransactionType.TRANSFER))
                .amount(moneyTransferDto.getAmount())
                .build();
    }
}
