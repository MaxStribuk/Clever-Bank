package by.home.service.impl;

import by.home.dao.api.IAccountDao;
import by.home.service.api.IAccountService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountDao accountDao;
}
