package com.solstice.accountaddress.service;

import com.solstice.accountaddress.dao.AccountRepository;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountAddressService {

  private Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

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
    Account newAccount = null;
    try {
      newAccount = objectMapper.readValue(data, Account.class);
      newAccount = accountRepository.save(newAccount);
    } catch (IOException e) {
      logger.error("IOException thrown in createAccount: {}", e.toString());
    }
    return newAccount;
  }

  public Account updateAccount(long id, String data) {
    Account updatedAccount = null;
    try {
      updatedAccount = objectMapper.readValue(data, Account.class);
      updatedAccount.setId(id);
      updatedAccount = accountRepository.save(updatedAccount);
    } catch (IOException e) {
      logger.error("IOException thrown in createAccount: {}", e.toString());
    }
    return updatedAccount;
  }

  public Account deleteAccount(long id) {
    Account deletedAccount = accountRepository.findAccountById(id);
    accountRepository.delete(deletedAccount);
    return deletedAccount;
  }

  public List<Address> getAddressesByAccountId(long id) {
    return accountRepository.findAddressesById(id);
  }

  public List<Address> createAddress(long id) {
    return Arrays.asList(new Address());
  }
}
