package by.home.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, представляющий собой сущность транзакция в базе данных
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction implements Serializable {

    private UUID id;
    private String accountFrom;
    private String accountTo;
    private LocalDateTime time;
    private BigDecimal amount;
    private short typeId;
}
