package com.solstice.accountaddress.service;

import com.google.gson.Gson;
import com.solstice.accountaddress.dao.AccountRepository;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AccountAddressService {

  private ObjectMapper objectMapper;

  private AccountRepository accountRepository;

  public AccountAddressService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
    objectMapper = new ObjectMapper();
  }

  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  public Account createAccount(String data) {
    Account newAccount = new Account();
    return newAccount;
  }

  public Account updateAccount(long id, String data) {
    return new Account();
  }

  public Account deleteAccount(long id) {
    return new Account();
  }

  public List<Address> getAddressesByAccountId(long id) {
    return accountRepository.findAddressesById(id);
  }

  public List<Address> createAddress(long id) {
    return Arrays.asList(new Address());
  }
}
