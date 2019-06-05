package spring.boot.apache.camel.saga.in.memory.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankA;

@Repository
public interface AccountBankARepository extends CrudRepository<AccountBankA, Long> {
}
