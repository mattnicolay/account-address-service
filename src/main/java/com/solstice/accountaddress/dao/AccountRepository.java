package com.solstice.accountaddress.dao;


import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
  List<Account> findAll();

  Account findAccountById(long id);

  List<Address> findAddressesById(long id);
}
