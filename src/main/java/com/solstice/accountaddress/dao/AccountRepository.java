package com.solstice.accountaddress.dao;


import com.solstice.accountaddress.model.Account;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
  List<Account> findAll();
}
