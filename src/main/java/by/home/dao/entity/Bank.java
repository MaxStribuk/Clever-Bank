package by.home.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс, представляющий собой сущность банк в базе данных
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bank implements Serializable {

    private short id;
    private String name;
}
