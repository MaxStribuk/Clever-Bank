package by.home.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Класс, представляющий собой перечисление возможных типов транзакций
 */
@AllArgsConstructor
@Getter
public enum TransactionType implements Serializable {

    REFILL("Пополнение"),
    WITHDRAWAL("Снятие"),
    TRANSFER("Перевод"),
    INTEREST_CALCULATION("Начисление процентов");

    private final String description;

    public static short getId(TransactionType type) {
        return (short) (type.ordinal() + 1);
    }

    public static TransactionType getTransactionType(short id) {
        return TransactionType.values()[id];
    }
}
