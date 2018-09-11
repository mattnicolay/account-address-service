package com.solstice.accountaddress.controller;

import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import com.solstice.accountaddress.service.AccountAddressService;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountAddressController {

  private AccountAddressService accountAddressService;

  public AccountAddressController(AccountAddressService accountAddressService) {
    this.accountAddressService = accountAddressService;
  }

  @GetMapping
  public ResponseEntity<List<Account>> getAccounts() {
    List<Account> accounts = accountAddressService.getAccounts();
    return new ResponseEntity<>(
        accounts,
        new HttpHeaders(),
        accounts.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Account> getAccountById(@PathVariable("id") long id) {
    Account account = accountAddressService.getAccountById(id);
    return new ResponseEntity<>(
        account,
        new HttpHeaders(),
        account == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Account> createAccount(@RequestBody String body) {
    Account account = accountAddressService.createAccount(body);
    return new ResponseEntity<>(
        account,
        new HttpHeaders(),
        account == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED
    );
  }

  @PutMapping("{id}")
  public ResponseEntity<Account> updateAccount(
      @PathVariable("id") long id,
      @RequestBody String body) {
    Account account = accountAddressService.updateAccount(id, body);
    return new ResponseEntity<>(
        account,
        new HttpHeaders(),
        account == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK
    );
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Account> deleteAccount(@PathVariable("id") long id) {
    Account account = accountAddressService.deleteAccount(id);
    return new ResponseEntity<>(
        account,
        new HttpHeaders(),
        account == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK
    );
  }

  @GetMapping("/{id}/address")
  public ResponseEntity<List<Address>> getAddress(@PathVariable("id") long id) {
    List<Address> addresses = accountAddressService.getAddressesByAccountId(id);
    return new ResponseEntity<>(
        addresses,
        new HttpHeaders(),
        addresses.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
  }

  @PostMapping("/{id}/address")
  public ResponseEntity<List<Address>> createAddress(
      @PathVariable("id") long id,
      @RequestBody String body) {
    List<Address> addresses = accountAddressService.createAddress(id, body);
    return new ResponseEntity<>(
        addresses,
        new HttpHeaders(),
        addresses.isEmpty() ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED);
  }

  @PutMapping("/{accountId}/address/{addressId}")
  public ResponseEntity<Address> updateAddress(
      @PathVariable("accountId") long accountId,
      @PathVariable("addressId") long addressId,
      @RequestBody String body) {
    Address address = accountAddressService.updateAddress(accountId, addressId, body);
    return new ResponseEntity<>(
        address,
        new HttpHeaders(),
        address == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
  }

  @DeleteMapping("/{accountId}/address/{addressId}")
  public ResponseEntity<Address> deleteAddress(
      @PathVariable("accountId") long accountId,
      @PathVariable("addressId") long addressId) {
    Address address = accountAddressService.deleteAddress(accountId, addressId);

    return new ResponseEntity<>(
        address,
        new HttpHeaders(),
        address == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
    );
  }
}
