package spring.boot.apache.camel.saga.in.memory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankB;
import spring.boot.apache.camel.saga.in.memory.repository.AccountBankBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountBankBService {

    @Autowired
    AccountBankBRepository accountBankBRepository;

    public List<AccountBankB> findAll() {
        List<AccountBankB> accounts = new ArrayList<>();
        accountBankBRepository.findAll().forEach(accounts::add);
        return accounts;
    }

    public AccountBankB findOne(Long id) {
        Optional<AccountBankB> account = accountBankBRepository.findById(id);
        if (account.isPresent())
            return account.get();
        else
            return new AccountBankB(-1L, -1L, -1L, "Unknown", "Unknown");
    }

    public AccountBankB increaseAmount(Long id, Long add) {
        AccountBankB account = findOne(id);

        // exception handling
        if (account.getId() == -1)
            throw new IllegalStateException("Bank B account #" + id + " does not exist");
        if (!"ACTIVE".equals(account.getStatus()))
            throw new IllegalStateException("Bank B account #" + id + " is not ACTIVE");

        account.setAmount(account.getAmount() + add);
        return accountBankBRepository.save(account);
    }

    public AccountBankB decreaseAmount(Long id, Long subtract) throws RuntimeException {
        AccountBankB account = findOne(id);
        if (account.getId() != -1 && "ACTIVE".equals(account.getStatus())) {
            account.setAmount(account.getAmount() - subtract);
            accountBankBRepository.save(account);
        }
        return account;
    }
}
