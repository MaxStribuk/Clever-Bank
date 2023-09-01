package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.dao.entity.Account;
import by.home.dao.entity.Client;
import by.home.dao.entity.Transaction;
import by.home.dao.entity.TransactionType;
import by.home.data.dto.AccountStatementDto;
import by.home.service.api.IAccountService;
import by.home.service.api.IBankService;
import by.home.service.api.IClientService;
import by.home.service.api.IPdfService;
import by.home.service.api.IStatementService;
import by.home.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static by.home.util.Constant.Utils.DATE_FORMATTER;
import static by.home.util.Constant.Utils.DATE_TIME_FORMATTER;
import static by.home.util.Constant.Utils.DATE_TIME_PATTERN;
import static by.home.util.Constant.Utils.DETAILED_STATEMENT_TEMPLATE;
import static by.home.util.Constant.Utils.GENERAL_STATEMENT_TEMPLATE;
import static by.home.util.Constant.Utils.STATEMENT_FILE_NAME;
import static by.home.util.Constant.Utils.TRANSACTION_TEMPLATE;

/**
 * сервисный класс для создания выписки по счету
 */
@RequiredArgsConstructor
public class StatementService implements IStatementService {

    private final IAccountService accountService;
    private final ITransactionService transactionService;
    private final Validator validator;
    private final IPdfService pdfService;
    private final IBankService bankService;
    private final IClientService clientService;

    /**
     * базовый метод для генерации выписки
     * @param accountStatementDto объект с информацией, необходимой для создания выписки
     */
    @Override
    @Loggable
    public void createStatement(AccountStatementDto accountStatementDto) {
        validate(accountStatementDto);
        Account account = this.accountService.findByAccountNumber(accountStatementDto.getAccount());
        LocalDateTime now = LocalDateTime.now();
        setValidDates(accountStatementDto, account, now);
        String fileName = String.format(STATEMENT_FILE_NAME, account.getClientId(),
                now.format(DATE_TIME_PATTERN));
        String statement = getStatement(accountStatementDto, account, now);
        this.pdfService.createPdf(statement, fileName);
    }

    private void setValidDates(AccountStatementDto accountStatementDto, Account account, LocalDateTime now) {
        if (accountStatementDto.getDateFrom() == null) {
            accountStatementDto.setDateFrom(account.getOpenDate());
        }
        if (accountStatementDto.getDateTo() == null) {
            accountStatementDto.setDateTo(now.toLocalDate());
        }
    }

    private void validate(AccountStatementDto accountStatementDto) {
        Set<ConstraintViolation<AccountStatementDto>> violations = this.validator.validate(accountStatementDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private String getStatement(AccountStatementDto accountStatementDto, Account account, LocalDateTime now) {
        List<Transaction> transactions = this.transactionService.getTransactions(accountStatementDto);
        String bankName = this.bankService
                .findById(account.getBankId())
                .getName().trim();
        Client client = this.clientService.getClientById(account.getClientId());
        return accountStatementDto.isDetailed()
                ? getDetailedStatement(accountStatementDto, bankName, account, transactions, now, client)
                : getGeneralStatement(accountStatementDto, bankName, account, transactions, now, client);
    }

    private String getGeneralStatement(
            AccountStatementDto accountStatementDto, String bankName, Account account,
            List<Transaction> transactions, LocalDateTime now, Client client) {
        return String.format(GENERAL_STATEMENT_TEMPLATE,
                bankName,
                client.getName().trim(),
                account.getNumber(),
                account.getOpenDate().format(DATE_FORMATTER),
                accountStatementDto.getDateFrom().format(DATE_FORMATTER),
                accountStatementDto.getDateTo().format(DATE_FORMATTER),
                now.format(DATE_TIME_FORMATTER),
                account.getBalance(),
                transactions.stream()
                        .filter(transaction -> accountStatementDto.getAccount()
                                .equals(transaction.getAccountTo()))
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                transactions.stream()
                        .filter(transaction -> accountStatementDto.getAccount()
                                .equals(transaction.getAccountFrom()))
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private String getDetailedStatement(
            AccountStatementDto accountStatementDto, String bankName, Account account,
            List<Transaction> transactions, LocalDateTime now, Client client) {
        StringBuilder detailedStatement = new StringBuilder(
                String.format(DETAILED_STATEMENT_TEMPLATE,
                        bankName,
                        client.getName().trim(),
                        account.getNumber(),
                        account.getOpenDate().format(DATE_FORMATTER),
                        accountStatementDto.getDateFrom().format(DATE_FORMATTER),
                        accountStatementDto.getDateTo().format(DATE_FORMATTER),
                        now.format(DATE_TIME_FORMATTER),
                        account.getBalance()
                )
        );
        transactions.forEach(transaction -> detailedStatement
                        .append(String.format(TRANSACTION_TEMPLATE,
                                transaction.getTime().format(DATE_FORMATTER),
                                TransactionType.getTransactionType(transaction.getTypeId()).getDescription(),
                                transaction.getAmount()
                        ))
                );
        return detailedStatement.toString();
    }
}
