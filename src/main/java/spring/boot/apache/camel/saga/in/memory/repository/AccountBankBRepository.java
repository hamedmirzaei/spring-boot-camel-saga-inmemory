package spring.boot.apache.camel.saga.in.memory.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.boot.apache.camel.saga.in.memory.model.AccountBankB;

@Repository
public interface AccountBankBRepository extends CrudRepository<AccountBankB, Long> {
}
