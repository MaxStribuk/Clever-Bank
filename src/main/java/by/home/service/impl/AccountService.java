package by.home.service.impl;

import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
import by.home.dao.entity.Transaction;
import by.home.dao.entity.TransactionType;
import by.home.data.dto.ChangeBalanceDto;
import by.home.data.exception.AccountNotFoundException;
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

    public void changeBalance(ChangeBalanceDto changeBalanceDto) {
        Set<ConstraintViolation<ChangeBalanceDto>> violations = validator.validate(changeBalanceDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Account account = accountDao.findById(changeBalanceDto.getAccount())
                .orElseThrow(() -> new AccountNotFoundException(
                        "account not found " + changeBalanceDto.getAccount()));
        BigDecimal currentBalance = account.getBalance();
        BigDecimal amount = changeBalanceDto.getAmount();
        BigDecimal newBalance = currentBalance.add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("not enough money on balance");
        }
        account.setBalance(newBalance);
        accountDao.update(account);
        transactionService.add(getTransaction(changeBalanceDto));
    }

    private Transaction getTransaction(ChangeBalanceDto changeBalanceDto) {
        TransactionType type = changeBalanceDto.getAmount().compareTo(BigDecimal.ZERO) > 0
                ? TransactionType.REFILL
                : TransactionType.WITHDRAWAL;
        String account = changeBalanceDto.getAccount();
        return Transaction.builder()
                .accountTo(account)
                .accountFrom(account)
                .time(LocalDateTime.now())
                .id(UUID.randomUUID())
                .typeId(TransactionType.getId(type))
                .amount(changeBalanceDto.getAmount())
                .build();
    }
}
