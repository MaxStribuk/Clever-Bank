package by.home.dao.entity;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public enum TransactionType implements Serializable {

    REFILL("Пополнение"),
    WITHDRAWAL("Снятие"),
    TRANSFER("Перевод"),
    INTEREST_CALCULATION("Начисление процентов");

    private final String description;

    public static short getId(TransactionType type) {
        return (short) (type.ordinal() + 1);
    }
}
