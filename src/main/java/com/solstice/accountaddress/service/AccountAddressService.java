package com.solstice.accountaddress.service;

import com.solstice.accountaddress.dao.AccountRepository;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountAddressService {

  private AccountRepository accountRepository;

  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  public List<Address> getAddressesByAccountId(long id) {
    return Arrays.asList(new Address());
  }

  public Account createAccount(long id) {
    return new Account();
  }

  public Account updateAccount(long id) {
    return new Account();
  }

  public Account deleteAccount(long id) {
    return new Account();
  }

  public List<Address> createAddress(long id) {
    return Arrays.asList(new Address());
  }
}
