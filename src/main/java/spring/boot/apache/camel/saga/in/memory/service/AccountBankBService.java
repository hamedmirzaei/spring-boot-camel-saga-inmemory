package spring.boot.apache.camel.saga.in.memory.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankB;
import spring.boot.apache.camel.saga.in.memory.repository.AccountBankBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountBankBService {

    @Autowired
    AccountBankBRepository accountRepository;

    public List<AccountBankB> findAll() {
        List<AccountBankB> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(accounts::add);
        return accounts;
    }

    public AccountBankB findOne(Long id) {
        Optional<AccountBankB> account = accountRepository.findById(id);
        if (account.isPresent())
            return account.get();
        else
            return new AccountBankB(-1L, -1L, -1L, "Unknown", "Unknown");
    }

    public AccountBankB increaseAmount(Long id, Long add) {
        AccountBankB account = findOne(id);
        if (account.getId() != -1) {
            account.setAmount(account.getAmount() + add);
            return accountRepository.save(account);
        } else {
            return account;
        }
    }
}
