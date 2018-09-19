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

  private Logger logger = LoggerFactory.getLogger(this.getClass());

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

  public Account createAccount(Account newAccount) {
    if (newAccount != null) {
      newAccount = accountRepository.save(newAccount);
    }
    return newAccount;
  }

  public Account updateAccount(long id, Account updatedAccount) {
    if (updatedAccount != null) {
      updatedAccount.setId(id);
      updatedAccount = accountRepository.save(updatedAccount);
    }
    return updatedAccount;
  }

  public Account deleteAccount(long id) {
    Account deletedAccount = getAccountById(id);
    if(deletedAccount != null) {
      accountRepository.delete(deletedAccount);
    }
    return deletedAccount;
  }

  public List<Address> getAddressesByAccountId(long id) {
    return accountRepository.findAddressesByAccountId(id);
  }

  public Address getAddressByAccountIdAndAddressId(long accountId, long addressId) {
    return addressRepository.findAddressByIdAndAccountId(addressId, accountId);
  }

  public Address createAddress(long id, Address address) {
    Account account = accountRepository.findAccountById(id);
    if (account == null) {
      return null;
    }
    account.addAddress(address);
    accountRepository.save(account);
    return address;
  }

  public Address updateAddress(long accountId, long addressId, Address updatedAddress) {
    Address dbAddress = addressRepository.findAddressByIdAndAccountId(addressId, accountId);
    if(dbAddress == null || updatedAddress == null) {
      return null;
    }
    updatedAddress.setId(addressId);
    addressRepository.save(updatedAddress);

    return updatedAddress;
  }

  public Address deleteAddress(long accountId, long addressId) {
    Address deletedAddress = addressRepository.findAddressByIdAndAccountId(addressId, accountId);
    if (deletedAddress != null) {
      addressRepository.delete(deletedAddress);
    }
    return deletedAddress;
  }
}
