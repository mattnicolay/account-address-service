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

  public Account getAccountById(long id) {
    return accountRepository.findAccountById(id);
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
    Account deletedAccount = getAccountById(id);
    accountRepository.delete(deletedAccount);
    return deletedAccount;
  }

  public List<Address> getAddressesByAccountId(long id) {
    return accountRepository.findAddressesById(id);
  }

  public List<Address> createAddress(long id, String body) {
    return Arrays.asList(new Address());
  }

  public Address getAddressById(int accountId, int addressId) {
    return new Address();
  }

  public Address updateAddress(long accountId, long addressId, String body) {
    return new Address();
  }

  public Address deleteAddress(long accountId, long addressId) {
    return new Address();
  }
}
