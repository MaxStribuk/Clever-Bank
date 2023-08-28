package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.aop.api.Transactional;
import by.home.dao.api.IAccountDao;
import by.home.dao.api.IBankDao;
import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Account;
import by.home.dao.entity.IsolationLevel;
import by.home.dao.entity.Transaction;
import by.home.dao.entity.TransactionType;
import by.home.data.dto.BankDto;
import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;
import by.home.data.dto.TransactionDto;
import by.home.data.exception.AccountNotFoundException;
import by.home.data.exception.InvalidArgsException;
import by.home.data.exception.InvalidBalanceException;
import by.home.service.api.IAccountService;
import by.home.service.api.IBankService;
import by.home.service.api.ICheckService;
import by.home.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountDao accountDao;
    private final Validator validator;
    private final ITransactionService transactionService;
    private final ICheckService checkService;
    private final ModelMapper modelMapper;
    private final IBankService bankService;

    @Override
    @Transactional(daoInterfaces = {IAccountDao.class, ITransactionDao.class, IBankDao.class})
    @Loggable
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
        Transaction transaction = getTransaction(changeBalanceDto);
        transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, account, account);
        checkService.createCheck(transactionDto);
    }

    @Override
    @Transactional(daoInterfaces = {IAccountDao.class, ITransactionDao.class, IBankDao.class},
            isolation = IsolationLevel.TRANSACTION_SERIALIZABLE)
    @Loggable
    public void transferMoney(MoneyTransferDto moneyTransferDto) {
        validate(moneyTransferDto);
        BigDecimal amount = moneyTransferDto.getAmount();
        Account accountFrom = accountDao.findById(moneyTransferDto.getAccountFrom())
                .orElseThrow(() -> new AccountNotFoundException("account not found"));
        Account accountTo = accountDao.findById(moneyTransferDto.getAccountTo())
                .orElseThrow(() -> new AccountNotFoundException("account not found"));
        Transaction transaction = getTransaction(moneyTransferDto);
        sort(moneyTransferDto);
        writeOffMoney(accountFrom, amount);
        addMoney(accountTo, amount);
        transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, accountFrom, accountTo);
        checkService.createCheck(transactionDto);
    }

    private TransactionDto getTransactionDto(Transaction transaction, Account accountFrom, Account accountTo) {
        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
        transactionDto.setTypeId((short) (transactionDto.getTypeId() - 1));
        short bankFromId = accountFrom.getBankId();
        short bankToId = accountTo.getBankId();
        BankDto bankFrom = bankService.findById(bankFromId);
        BankDto bankTo = bankFromId == bankToId
                ? bankFrom
                : bankService.findById(bankToId);
        transactionDto.setBankFrom(bankFrom.getName().trim());
        transactionDto.setBankTo(bankTo.getName().trim());
        return transactionDto;
    }

    private void sort(MoneyTransferDto moneyTransferDto) {
        String accountFrom = moneyTransferDto.getAccountFrom();
        String accountTo = moneyTransferDto.getAccountTo();
        if (accountFrom.compareTo(accountTo) > 0) {
            moneyTransferDto.setAccountFrom(accountTo);
            moneyTransferDto.setAccountTo(accountFrom);
            moneyTransferDto.setAmount(moneyTransferDto.getAmount().negate());
        }
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
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        BigDecimal amount = changeBalanceDto.getAmount();
        validateAmount(amount);
    }

    private void validate(MoneyTransferDto moneyTransferDto) {
        Set<ConstraintViolation<MoneyTransferDto>> violations = validator.validate(moneyTransferDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (moneyTransferDto.getAccountTo().equals(moneyTransferDto.getAccountFrom())) {
            throw new InvalidArgsException("accountFrom must not equals accountTo");
        }
        BigDecimal amount = moneyTransferDto.getAmount();
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
