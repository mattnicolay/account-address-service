package com.solstice.accountaddress.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.accountaddress.dao.AccountRepository;
import com.solstice.accountaddress.dao.AddressRepository;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AccountAddressServiceUnitTest {

  private Logger logger = LoggerFactory.getLogger(AccountAddressServiceUnitTest.class);
  private final String CREATE = "CREATE";
  private final String UPDATE = "UPDATE";

  @Mock
  private AccountRepository accountRepository;
  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private AccountAddressService accountAddressService;

  private Account account1;
  private Account account2;
  private Account account3;

  private Address address1;
  private Address address2;
  private Address address3;


  @Before
  public void setup() {

    address1 = new Address(
        "111 N Canal St",
        "700",
        "Chicago",
        "IL",
        "60606",
        "United States"
    );
    address2 = new Address(
        "233 S Wacker Dr",
        "4305",
        "Chicago",
        "IL",
        "60606",
        "United States"
    );
    address3 = new Address(
        "405 Lexington Ave",
        "203",
        "New York",
        "NY",
        "10174",
        "United States"
    );

    List<Address> addresses = new ArrayList<>();
    addresses.add(address1);
    addresses.add(address2);
    addresses.add(address3);

    account1 = new Account("Jane", "Doe", "jdoe@gmail.com", addresses.subList(1,2));
    account2 = new Account("John", "Smith", "jsmith@gmail.com", addresses.subList(2,3));
    account3 = new Account("Bill", "Murray", "bmurray@gmail.com", addresses);


  }

  @Test
  public void getAccountsReturnedListHasValuesTest() {
    when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2, account3));
    List<Account> accounts = accountAddressService.getAccounts();

    assertFalse(accounts.isEmpty());
    accounts.forEach(account -> {
      assertThat(account, is(notNullValue()));
      assertThat(account.getFirstName(), is(notNullValue()));
      assertThat(account.getLastName(), is(notNullValue()));
      assertThat(account.getEmail(), is(notNullValue()));
      assertThat(account.getAddresses(), is(notNullValue()));
    });
  }

  @Test
  public void getAccountsFailureTest() {
    List<Account> accounts = accountAddressService.getAccounts();

    assertThat(accounts, is(notNullValue()));
    assertTrue(accounts.isEmpty());
  }

  @Test
  public void getAccountByIdReturnedAccountHasValuesTest() {
    when(accountRepository.findAccountById(anyLong())).thenReturn(account1);
    Account account = accountAddressService.getAccountById(1);

    assertThatAccountsAreEqual(account, account1);
  }

  @Test
  public void getAccountByIdFailureTest() {
    Account account = accountAddressService.getAccountById(1);

    assertThat(account, is(nullValue()));
  }

  @Test
  public void createAccountReturnedAccountHasValuesTest() throws IOException {
    when(accountRepository.save(any())).thenReturn(account1);
    Account account = accountAddressService.createAccount(toJson(account1));

    assertThatAccountsAreEqual(account, account1);
  }

  @Test
  public void createAccountFailureTest() throws IOException {
    Account account = accountAddressService.createAccount(toJson(account1));

    assertThat(account, is(nullValue()));
  }

  @Test(expected = IOException.class)
  public void createAccountJacksonFailureTest() throws IOException {
    accountAddressService.createAccount("{wrong format)");
  }

  @Test
  public void updateAccountReturnedAccountHasValuesTest() throws IOException {
    when(accountRepository.save(any())).thenReturn(account1);
    Account account = accountAddressService.updateAccount(1, toJson(account1));

    assertThatAccountsAreEqual(account, account1);
  }

  @Test
  public void updateAccountRepositoryFailureTest() throws IOException {
    Account account = accountAddressService.updateAccount(1, toJson(account1));

    assertThat(account, is(nullValue()));

  }

  @Test(expected = IOException.class)
  public void updateAccountJacksonFailureTest() throws IOException {
    accountAddressService.updateAccount(1, "{wrong format)");
  }

  @Test
  public void deleteAccountReturnedAccountHasValuesTest() {
    when(accountRepository.findAccountById(anyLong())).thenReturn(account1);
    Account account = accountAddressService.deleteAccount(1);

    assertThat(account, is(notNullValue()));
    assertThatAccountsAreEqual(account, account1);
  }

  @Test
  public void deleteAccountFailureTest() {
    Account account = accountAddressService.deleteAccount(1);

    assertThat(account, is(nullValue()));
  }

  @Test
  public void getAddressesByAccountIdReturnedListHasValuesTest() {
    when(accountRepository.findAddressesByAccountId(anyLong())).thenReturn(Arrays.asList(
        address1,
        address2,
        address3
    ));
    List<Address> addresses = accountAddressService.getAddressesByAccountId(1);

    assertThat(addresses, is(notNullValue()));
    assertFalse(addresses.isEmpty());
    addresses.forEach(address -> {
      assertThat(address, is(notNullValue()));
      assertThat(address.getStreet(), is(notNullValue()));
      assertThat(address.getApartment(), is(notNullValue()));
      assertThat(address.getCity(), is(notNullValue()));
      assertThat(address.getState(), is(notNullValue()));
      assertThat(address.getZip(), is(notNullValue()));
      assertThat(address.getCountry(), is(notNullValue()));
    });
  }

  @Test
  public void getAddressesByAccountIdFailureTest() {
    List<Address> addresses = accountAddressService.getAddressesByAccountId(1);

    assertThat(addresses, is(notNullValue()));
    assertTrue(addresses.isEmpty());
  }

  @Test
  public void getAddressByAccountIdAndAddressIdReturnedAddressHasValuesTest() {
    when(addressRepository.findAddressByIdAndAccountId(anyLong(), anyLong())).thenReturn(address1);
    Address address = accountAddressService.getAddressByAccountIdAndAddressId(1, 1);

    assertThatAddressesAreEqual(address, address1);
  }

  @Test
  public void getAddressByAccountIdAndAddressIdFailureTest() {
    Address address = accountAddressService.getAddressByAccountIdAndAddressId(1, 1);

    assertThat(address, is(nullValue()));
  }

  @Test
  public void createAddressReturnedAddressHasValuesTest() throws IOException {
    when(accountRepository.findAccountById(anyLong())).thenReturn(account1);
    Address address = accountAddressService.createAddress(1, toJson(address1));

    assertThatAddressesAreEqual(address, address1);
  }

  @Test
  public void createAddressAccountNotFoundTest() throws IOException {
    Address address = accountAddressService.createAddress(4, toJson(address1));

    assertThat(address, is(nullValue()));
  }

  @Test(expected = IOException.class)
  public void createAddressJsonParseFailureTest() throws IOException {
    accountAddressService.createAddress(4, "{wrong format)");
  }

  @Test
  public void updateAddressReturnedAddressHasValuesTest() throws IOException {
    when(addressRepository.findAddressByIdAndAccountId(anyLong(), anyLong())).thenReturn(address1);
    Address address = accountAddressService.updateAddress(1, 1, toJson(address1));

    assertThatAddressesAreEqual(address, address1);
  }

  @Test
  public void updateAddressNotFoundTest() throws IOException {
    Address address = accountAddressService.updateAddress(1, 3, toJson(address1));

    assertThat(address, is(nullValue()));
  }

  @Test(expected = IOException.class)
  public void updateAddressJsonParseFailureTest() throws IOException {
    when(addressRepository.findAddressByIdAndAccountId(1, 1)).thenReturn(address1);
    accountAddressService.updateAddress(1, 1, "{wrong format}");
  }

  @Test
  public void deleteAddressReturnedAddressHasValuesTest() {
    when(addressRepository.findAddressByIdAndAccountId(anyLong(), anyLong())).thenReturn(address1);
    Address address = accountAddressService.deleteAddress(1, 1);

    assertThatAddressesAreEqual(address, address1);
  }

  @Test
  public void deleteAddress_InvalidIds_ReturnsNull() {
    assertThat(accountAddressService.deleteAddress(6, -1), is(nullValue()));
  }

  private void assertThatAddressesAreEqual(Address actual, Address expected) {
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getStreet(), is(notNullValue()));
    assertThat(actual.getStreet(), is(equalTo(expected.getStreet())));
    assertThat(actual.getApartment(), is(notNullValue()));
    assertThat(actual.getApartment(), is(equalTo(expected.getApartment())));
    assertThat(actual.getCity(), is(notNullValue()));
    assertThat(actual.getCity(), is(equalTo(expected.getCity())));
    assertThat(actual.getState(), is(notNullValue()));
    assertThat(actual.getState(), is(equalTo(expected.getState())));
    assertThat(actual.getZip(), is(notNullValue()));
    assertThat(actual.getZip(), is(equalTo(expected.getZip())));
    assertThat(actual.getCountry(), is(notNullValue()));
    assertThat(actual.getCountry(), is(equalTo(expected.getCountry())));
  }

  private void assertThatAccountsAreEqual(Account actual, Account expected) {
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getId(), is(equalTo(expected.getId())));
    assertThat(actual.getFirstName(), is(notNullValue()));
    assertThat(actual.getFirstName(), is(equalTo(expected.getFirstName())));
    assertThat(actual.getLastName(), is(notNullValue()));
    assertThat(actual.getLastName(), is(equalTo(expected.getLastName())));
    assertThat(actual.getEmail(), is(notNullValue()));
    assertThat(actual.getEmail(), is(equalTo(expected.getEmail())));
    assertThat(actual.getAddresses(), is(notNullValue()));
    assertThat(actual.getAddresses(), is(equalTo(expected.getAddresses())));
  }

  private String toJson(Object object) {
    ObjectMapper objectMapper = new ObjectMapper();
    String result = null;
    try {
      result = objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
    return result;
  }
}
