package by.home.dao.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SqlQuery {

        public static final String FIND_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM account WHERE number=?;";
        public static final String FIND_BANK_BY_BANK_ID = "SELECT * FROM bank WHERE id=?;";
        public static final String UPDATE_ACCOUNT = "UPDATE account SET balance=? WHERE number=?;";
        public static final String FIND_TRANSACTION_BY_TRANSACTION_ID = "SELECT * FROM transaction WHERE id=?;";
        public static final String FIND_TRANSACTION_BY_ACCOUNT =
                "SELECT * FROM transaction WHERE account_from=? OR account_to=?;";
        public static final String FIND_ALL_TRANSACTION = "SELECT * FROM transaction;";
        public static final String INSERT_TRANSACTION = "INSERT INTO transaction " +
                "(id, account_from, account_to, amount, time, type_id) VALUES (?,?,?,?,?,?);";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ColumnName {

        public static final String NUMBER = "number";
        public static final String BALANCE = "balance";
        public static final String CLIENT_ID = "client_id";
        public static final String BANK_ID = "bank_id";
        public static final String OPEN_DATE = "open_date";
        public static final String ID = "id";
        public static final String ACCOUNT_FROM = "account_from";
        public static final String ACCOUNT_TO = "account_to";
        public static final String TYPE_ID = "type_id";
        public static final String AMOUNT = "amount";
        public static final String TIME = "time";
        public static final String NAME = "name";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Utils {

        public static final String PROPERTIES_FILE_NAME = "application.yml";
        public static final String DAO_IMPL_PACKAGE_NAME = "by.home.dao.impl";
        public static final String DAO_API_PACKAGE_NAME = "by.home.dao.api";
        public static final String GET_INSTANCE_METHOD_NAME = "getInstance";
        public static final String SINGLETON_CLASS_NAME_PATTERN = "by.home.factory.dao.%sSingleton";
        public static final String LOGGING_FILE_NAME = "logging.txt";
        public static final String LOGGING_MESSAGE_PATTERN = "Method %s args %s return value %s\n";
        public static final String DATE_PATTERN = "dd-MM-yyyy";
        public static final String CHECK_FILE_NAME = "check/%s.pdf";
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
    }
}
