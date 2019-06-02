package spring.boot.apache.camel.saga.in.memory.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankA;
import spring.boot.apache.camel.saga.in.memory.repository.AccountBankARepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountBankAService {

    @Autowired
    AccountBankARepository accountRepository;

    public List<AccountBankA> findAll() {
        List<AccountBankA> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(accounts::add);
        return accounts;
    }

    public AccountBankA findOne(Long id) {
        Optional<AccountBankA> account = accountRepository.findById(id);
        if (account.isPresent())
            return account.get();
        else
            return new AccountBankA(-1L, -1L, -1L, "Unknown", "Unknown");
    }

    public AccountBankA decreaseAmount(Long id, Long subtract) {
        AccountBankA account = findOne(id);
        if (account.getId() != -1) {
            if (account.getAmount() < subtract)
                throw new IllegalArgumentException("Could not decrease amount because there is not enough amount available");
            account.setAmount(account.getAmount() - subtract);
            return accountRepository.save(account);
        } else {
            return account;
        }
    }
}
