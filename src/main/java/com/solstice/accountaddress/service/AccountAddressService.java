package com.solstice.accountaddress.service;

import com.solstice.accountaddress.dao.AccountRepository;
import com.solstice.accountaddress.dao.AddressRepository;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.io.IOException;
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
  private AddressRepository addressRepository;

  public AccountAddressService(AccountRepository accountRepository, AddressRepository addressRepository) {
    this.accountRepository = accountRepository;
    this.addressRepository = addressRepository;
    objectMapper = new ObjectMapper();
  }

  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  public Account getAccountById(long id) {
    return accountRepository.findAccountById(id);
  }

  public Account createAccount(String data) throws IOException {
    Account newAccount = getAccountFromJson(data);
    newAccount = accountRepository.save(newAccount);
    return newAccount;
  }

  public Account updateAccount(long id, String data) throws IOException {
    Account updatedAccount = getAccountFromJson(data);
    if (updatedAccount == null) {
      return null;
    }
    updatedAccount.setId(id);
    updatedAccount = accountRepository.save(updatedAccount);
    return updatedAccount;
  }

  public Account deleteAccount(long id) {
    Account deletedAccount = getAccountById(id);
    accountRepository.delete(deletedAccount);
    return deletedAccount;
  }

  public List<Address> getAddressesByAccountId(long id) {
    return accountRepository.findAddressesByAccountId(id);
  }

  public Address getAddressByAccountIdAndAddressId(long accountId, long addressId) {
    return addressRepository.findAddressByIdAndAccountId(addressId, accountId);
  }

  public Address createAddress(long id, String body) throws IOException {
    Address address= getAddressFromJson(body);
    Account account = accountRepository.findAccountById(id);
    if (account == null) {
      return null;
    }
    account.addAddress(address);
    accountRepository.save(account);
    return address;
  }

  public Address updateAddress(long accountId, long addressId, String body) throws IOException {
    Address updatedAddress = null;
    Address dbAddress = addressRepository.findAddressByIdAndAccountId(addressId, accountId);
    if(dbAddress != null) {
      updatedAddress = getAddressFromJson(body);
      updatedAddress.setId(addressId);
      addressRepository.save(updatedAddress);
    }
    return updatedAddress;
  }

  public Address deleteAddress(long accountId, long addressId) {
    Address deletedAddress = addressRepository.findAddressByIdAndAccountId(addressId, accountId);
    addressRepository.delete(deletedAddress);
    return deletedAddress;
  }

  private Account getAccountFromJson(String json) throws IOException {
    Account account = null;
    account = objectMapper.readValue(json, Account.class);
    return account;
  }

  private Address getAddressFromJson(String json) throws IOException {
    Address address = null;
    address = objectMapper.readValue(json, Address.class);
    return address;
  }
}
