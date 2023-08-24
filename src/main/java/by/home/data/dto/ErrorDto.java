package by.home.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class ErrorDto implements Serializable {

    private String field;
    private String message;
}

