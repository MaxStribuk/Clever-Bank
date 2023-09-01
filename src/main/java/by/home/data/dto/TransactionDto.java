package by.home.data.dto;

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
 * представляет dto класс для операции добавления транзакции
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionDto implements Serializable {

    private UUID id;
    private String bankFrom;
    private String accountFrom;
    private String bankTo;
    private String accountTo;
    private LocalDateTime time;
    private BigDecimal amount;
    private short typeId;
}
