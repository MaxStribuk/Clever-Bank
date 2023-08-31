package by.home.service.api;

import by.home.data.dto.AccountStatementDto;

public interface IStatementService {

    void createStatement(AccountStatementDto accountStatementDto);
}
