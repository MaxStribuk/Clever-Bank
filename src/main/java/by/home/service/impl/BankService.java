package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.dao.api.IBankDao;
import by.home.dao.entity.Bank;
import by.home.data.dto.BankDto;
import by.home.data.exception.BankNotFoundException;
import by.home.service.api.IBankService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * сервисный класс для операций с сущностью банк
 */
@RequiredArgsConstructor
public class BankService implements IBankService {

    private final IBankDao bankDao;
    private final ModelMapper modelMapper;

    @Override
    @Loggable
    public BankDto findById(short id) {
        Bank bank = this.bankDao.findById(id)
                .orElseThrow(() -> new BankNotFoundException("bank not found: " + id));
        return this.modelMapper.map(bank, BankDto.class);
    }
}
