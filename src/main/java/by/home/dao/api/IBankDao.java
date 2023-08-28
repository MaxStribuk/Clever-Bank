package by.home.dao.api;

import by.home.dao.entity.Bank;

import java.util.Optional;

public interface IBankDao {

    Optional<Bank> findById(short id);
}
