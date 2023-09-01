package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static by.home.util.Constant.ExceptionMessage.ACCOUNT_NOT_FOUND;
import static by.home.util.Constant.ExceptionMessage.INVALID_BALANCE;

/**
 * сервисный класс для работы со счетами клиентов
 */
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountDao accountDao;
    private final Validator validator;
    private final ITransactionService transactionService;
    private final ICheckService checkService;
    private final ModelMapper modelMapper;
    private final IBankService bankService;

    /**
     * метод, изменяющий баланс по конкретному счету
     *
     * @param changeBalanceDto объект со счетом для изменения
     *                         и суммой изменения
     */
    @Override
    @Loggable
    public void changeBalance(ChangeBalanceDto changeBalanceDto) {
        validate(changeBalanceDto);
        BigDecimal amount = changeBalanceDto.getAmount();
        Account account = findByAccountNumber(changeBalanceDto.getAccount());
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            writeOffMoney(account, amount.negate());
        } else {
            addMoney(account, amount);
        }
        this.accountDao.update(account);
        Transaction transaction = getTransaction(changeBalanceDto);
        this.transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, account, account);
        this.checkService.createCheck(transactionDto);
    }

    /**
     * метод для перевода денег с одного счета на другой
     *
     * @param moneyTransferDto объект, содержаний счета для денежного перевода
     *                         и сумму транзакции
     */
    @Override
    @Loggable
    public void transferMoney(MoneyTransferDto moneyTransferDto) {
        validate(moneyTransferDto);
        BigDecimal amount = moneyTransferDto.getAmount();
        Account accountFrom = findByAccountNumber(moneyTransferDto.getAccountFrom());
        Account accountTo = findByAccountNumber(moneyTransferDto.getAccountTo());
        Transaction transaction = getTransaction(moneyTransferDto);
        sort(moneyTransferDto);
        writeOffMoney(accountFrom, amount);
        addMoney(accountTo, amount);
        this.accountDao.transferMoney(accountFrom, accountTo);
        this.transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, accountFrom, accountTo);
        this.checkService.createCheck(transactionDto);
    }

    @Override
    @Loggable
    public List<Account> getAccountsForInterestAccrual(int count, boolean status) {
        return this.accountDao.getAccountsForInterestAccrual(count, status);
    }

    /**
     * метод для начисления процентов
     *
     * @param account счет, на который будет произведено нпчисление процентов
     * @param accrual сумма начисленных процентов
     */
    @Override
    @Loggable
    public void interestAccrual(Account account, BigDecimal accrual) {
        this.update(account);
        Transaction transaction = Transaction.builder()
                .accountTo(account.getNumber())
                .time(LocalDateTime.now())
                .id(UUID.randomUUID())
                .typeId(TransactionType.getId(TransactionType.INTEREST_CALCULATION))
                .amount(accrual)
                .build();
        this.transactionService.add(transaction);
        TransactionDto transactionDto = getTransactionDto(transaction, account, account);
        this.checkService.createCheck(transactionDto);
    }

    @Override
    @Loggable
    public void update(Account account) {
        this.accountDao.update(account);
    }

    @Override
    @Loggable
    public Account findByAccountNumber(String accountNumber) {
        return this.accountDao.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
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
    }

    private void addMoney(Account account, BigDecimal amount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        account.setBalance(newBalance);
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
        String account = changeBalanceDto.getAccount();
        TransactionType type;
        String accountFrom = null;
        String accountTo = null;
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            type = TransactionType.REFILL;
            accountTo = account;
        } else {
            type = TransactionType.WITHDRAWAL;
            accountFrom = account;
        }
        return Transaction.builder()
                .accountTo(accountTo)
                .accountFrom(accountFrom)
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
