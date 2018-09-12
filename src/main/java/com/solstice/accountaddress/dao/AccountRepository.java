package com.solstice.accountaddress.dao;


import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, Long> {
  List<Account> findAll();

  Account findAccountById(long id);

  @Query("select a.addresses from Account a where a.id = :id")
  List<Address> findAddressesByAccountId(@Param("id") long id);
}
