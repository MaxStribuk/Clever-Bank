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
import by.home.util.PropertiesUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static by.home.util.Constant.ExceptionMessage.ACCOUNT_NOT_FOUND;
import static by.home.util.Constant.ExceptionMessage.INVALID_BALANCE;
import static by.home.util.Constant.Utils.HUNDRED_PERCENT;
import static by.home.util.Constant.Utils.PERCENT_PROPERTY_NAME;

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
        Account account = this.accountDao.findById(changeBalanceDto.getAccount())
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            writeOffMoney(account, amount);
        } else {
            addMoney(account, amount);
        }
        Transaction transaction = getTransaction(changeBalanceDto);
        this.transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, account, account);
        this.checkService.createCheck(transactionDto);
    }

    @Override
    @Transactional(daoInterfaces = {IAccountDao.class, ITransactionDao.class, IBankDao.class},
            isolation = IsolationLevel.TRANSACTION_SERIALIZABLE)
    @Loggable
    public void transferMoney(MoneyTransferDto moneyTransferDto) {
        validate(moneyTransferDto);
        BigDecimal amount = moneyTransferDto.getAmount();
        Account accountFrom = this.accountDao.findById(moneyTransferDto.getAccountFrom())
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        Account accountTo = this.accountDao.findById(moneyTransferDto.getAccountTo())
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        Transaction transaction = getTransaction(moneyTransferDto);
        sort(moneyTransferDto);
        writeOffMoney(accountFrom, amount);
        addMoney(accountTo, amount);
        this.transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, accountFrom, accountTo);
        this.checkService.createCheck(transactionDto);
    }

    @Override
    @Loggable
    @Transactional(readOnly = true, daoInterfaces = IAccountDao.class)
    public List<Account> getAccountsForInterestAccrual(int count, boolean status) {
        return this.accountDao.getAccountsForInterestAccrual(count, status);
    }

    @Override
    @Loggable
    @Transactional(daoInterfaces = IAccountDao.class)
    public void update(Account account) {
        this.accountDao.update(account);
    }

    private TransactionDto getTransactionDto(Transaction transaction, Account accountFrom, Account accountTo) {
        TransactionDto transactionDto = this.modelMapper.map(transaction, TransactionDto.class);
        transactionDto.setTypeId((short) (transactionDto.getTypeId() - 1));
        short bankFromId = accountFrom.getBankId();
        short bankToId = accountTo.getBankId();
        BankDto bankFrom = this.bankService.findById(bankFromId);
        BankDto bankTo = bankFromId == bankToId
                ? bankFrom
                : this.bankService.findById(bankToId);
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
            throw new InvalidBalanceException(INVALID_BALANCE);
        }
        account.setBalance(newBalance);
        this.accountDao.update(account);
    }

    private void addMoney(Account account, BigDecimal amount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        account.setBalance(newBalance);
        this.accountDao.update(account);
    }

    private void validate(ChangeBalanceDto changeBalanceDto) {
        Set<ConstraintViolation<ChangeBalanceDto>> violations = this.validator.validate(changeBalanceDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        BigDecimal amount = changeBalanceDto.getAmount();
        validateAmount(amount);
    }

    private void validate(MoneyTransferDto moneyTransferDto) {
        Set<ConstraintViolation<MoneyTransferDto>> violations = this.validator.validate(moneyTransferDto);
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
