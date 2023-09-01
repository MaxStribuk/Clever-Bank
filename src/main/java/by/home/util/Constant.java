package by.home.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SqlQuery {

        public static final String FIND_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM account WHERE number=?;";
        public static final String FIND_ACCOUNTS_FOR_INTEREST_ACCRUAL =
                "SELECT * FROM account WHERE interest_accrued=? LIMIT ?;";
        public static final String FIND_BANK_BY_BANK_ID = "SELECT * FROM bank WHERE id=?;";
        public static final String UPDATE_ACCOUNT = "UPDATE account SET balance=?, interest_accrued=? WHERE number=?;";
        public static final String FIND_TRANSACTION_BY_ACCOUNT =
                "SELECT * FROM transaction WHERE time::date>=? AND time::date<=? AND (account_from=? OR account_to=?);";
        public static final String INSERT_TRANSACTION = "INSERT INTO transaction " +
                "(id, account_from, account_to, amount, time, type_id) VALUES (?,?,?,?,?,?);";
        public static final String FIND_CLIENT_BY_ID = "SELECT * FROM client WHERE id=?;";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ColumnName {

        public static final String NUMBER = "number";
        public static final String BALANCE = "balance";
        public static final String CLIENT_ID = "client_id";
        public static final String BANK_ID = "bank_id";
        public static final String OPEN_DATE = "open_date";
        public static final String ID = "id";
        public static final String ACCOUNT = "account";
        public static final String ACCOUNT_FROM = "account_from";
        public static final String ACCOUNT_TO = "account_to";
        public static final String TYPE_ID = "type_id";
        public static final String AMOUNT = "amount";
        public static final String TIME = "time";
        public static final String NAME = "name";
        public static final String PASSPORT_NUMBER = "passport_number";
        public static final String INTEREST_ACCRUED = "interest_accrued";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ExceptionMessage {

        public static final String ACCOUNT_NOT_FOUND = "account not found";
        public static final String INVALID_BALANCE = "not enough money on balance";
        public static final String INVALID_ACCOUNT_NUMBER = "invalid account number";
        public static final String ACCOUNT_MUST_BE_POSITIVE = "amount must be positive";
        public static final String AMOUNT_MUST_BE_POSITIVE_OR_NEGATIVE = "amount must be positive or negative";
        public static final String INVALID_DIGITS =
                "the amount must have no more than 8 digits in the integer part and 2 in the fractional";
        public static final String INVALID_ACCOUNT_TO_NUMBER = "invalid accountTo number";
        public static final String INVALID_ACCOUNT_FROM_NUMBER = "invalid accountFrom number";
        public static final String INVALID_DATE = "date must be in the past or present";
        public static final String INVALID_DETAILED_PARAMETER = "detail parameter is required";
    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Utils {

        public static final String PROPERTIES_FILE_NAME = "application.yml";
        public static final String DB_URL_PROPERTIES_NAME = "url";
        public static final String DB_USERNAME_PROPERTIES_NAME = "username";
        public static final String DB_PASSWORD_PROPERTIES_NAME = "password";
        public static final String SQL_DRIVER_PROPERTIES_NAME = "driver";
        public static final String ACCOUNT_PATTERN = "^BY13[A-Z]{4}\\d{20}$";
        public static final String OPERATION_SUCCESSFUL = "Operation successful complete";
        public static final String LOGGING_FILE_NAME = "logging.txt";
        public static final String LOGGING_MESSAGE_PATTERN = "Method %s args %s return value %s\n";
        public static final String DATE_PATTERN = "dd-MM-yyyy";
        public static final String CHECK_FILE_NAME = "check/%s.pdf";
        public static final String STATEMENT_FILE_NAME = "statement-money/%s_%s.pdf";
        public static final String FONT_COURIER_NEW_PATH = "/fonts/courier-new.ttf";
        public static final String CHECK_TEMPLATE = """
                        --------------------------------------------------------------
                        |                         Банковский чек                     |
                        |                                                            |
                        |Чек:%56s|
                        |%-20s%40tT|
                        |Тип транзакции:%45s|
                        |Банк отправителя:%43s|
                        |Банк получателя:%44s|
                        |Счет отправителя:%43s|
                        |Счет получателя:%44s|
                        |Сумма:%50.2f BYN|
                        |------------------------------------------------------------|
                                            """;
        public static final int EXECUTOR_CORE_POOL_SIZE = 4;
        public static final int ACCOUNTS_FOR_INTEREST_ACCRUAL = 100;
        public static final long DELAY_BETWEEN_SHIPMENTS = 30L;
        public static final long INITIAL_DELAY = 30L;
        public static final String PERCENT_PROPERTY_NAME = "percents";
        public static final BigDecimal HUNDRED_PERCENT = BigDecimal.valueOf(100);
        public static final String DATE_FROM = "dateFrom";
        public static final String DATE_TO = "dateTo";
        public static final String DETAILED_FLAG = "detailed";
        public static final String GENERAL_STATEMENT_TEMPLATE = """
                                            Money statement
                        %30s
                        Клиент                    | %s
                        Счет                      | %s
                        Валюта                    | BYN
                        Дата открытия             | %s
                        Период                    | %s - %s
                        Дата и время формирования | %s
                        Остаток                   | %s BYN
                                     Приход       |       Уход
                        --------------------------------------------------------
                        %21s BYN | -%s BYN
                                            """;
        public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        public static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm-ss");
        public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, kk.mm");
        public static final String DETAILED_STATEMENT_TEMPLATE = """
                                                   Выписка
                        %30s
                        Клиент                    | %s
                        Счет                      | %s
                        Валюта                    | BYN
                        Дата открытия             | %s
                        Период                    | %s - %s
                        Дата и время формирования | %s
                        Остаток                   | %s BYN
                            Дата   |     Примечание       |       Сумма
                        --------------------------------------------------------
                                            """;
        public static final String TRANSACTION_TEMPLATE = "%s | %-20s | %s BYN\n";
    }
}
