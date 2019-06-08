package spring.boot.apache.camel.saga.in.memory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankA;
import spring.boot.apache.camel.saga.in.memory.repository.AccountBankARepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountBankAService {

    @Autowired
    AccountBankARepository accountBankARepository;

    public List<AccountBankA> findAll() {
        List<AccountBankA> accounts = new ArrayList<>();
        accountBankARepository.findAll().forEach(accounts::add);
        return accounts;
    }

    public AccountBankA findOne(Long id) {
        Optional<AccountBankA> account = accountBankARepository.findById(id);
        if (account.isPresent())
            return account.get();
        else
            return new AccountBankA(-1L, -1L, -1L, "Unknown", "Unknown");
    }

    public AccountBankA increaseAmount(Long id, Long add) {
        AccountBankA account = findOne(id);
        if (account.getId() != -1 && "ACTIVE".equals(account.getStatus())) {
            account.setAmount(account.getAmount() + add);
            return accountBankARepository.save(account);
        }
        return account;
    }

    public AccountBankA decreaseAmount(Long id, Long subtract) throws RuntimeException {
        AccountBankA account = findOne(id);

        // exception handling
        if (account.getId() == -1)
            throw new IllegalStateException("Bank A account #" + id + " does not exist");
        if (!"ACTIVE".equals(account.getStatus()))
            throw new IllegalStateException("Bank A account #" + id + " is not ACTIVE");

        account.setAmount(account.getAmount() - subtract);
        accountBankARepository.save(account);
        if (account.getAmount() < 0)
            throw new IllegalArgumentException("Bank A account #" + id + " amount is less than " + subtract);

        return account;
    }
}
