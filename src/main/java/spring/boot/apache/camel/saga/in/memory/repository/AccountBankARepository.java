package spring.boot.apache.camel.saga.in.memory.repository;

import org.springframework.data.repository.CrudRepository;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankA;

public interface AccountBankARepository extends CrudRepository<AccountBankA, Long> {
}
