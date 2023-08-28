package by.home.service.api;

import by.home.data.dto.BankDto;

public interface IBankService {

    BankDto findById(short id);
}
