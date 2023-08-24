package by.home.dao.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SqlQuery {

        public static final String FIND_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM account WHERE number=?;";
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
    }
}
