package com.solstice.accountaddress.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.accountaddress.dao.AccountRepository;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
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

  @InjectMocks
  private AccountAddressService accountAddressService;

  private Account account1;
  private Account account2;
  private Account account3;

  private Address address1;
  private Address address2;
  private Address address3;

  private ObjectMapper objectMapper = new ObjectMapper();


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

    account1 = new Account("Jane", "Doe", "jdoe@gmail.com", Arrays.asList(address1, address2));
    account2 = new Account("John", "Smith", "jsmith@gmail.com", Arrays.asList(address2, address3));
    account3 = new Account("Bill", "Murray", "bmurray@gmail.com", Arrays.asList(address3, address1));


  }

  @Test
  public void getAccountsReturnedListHasValuesTest() {
    when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2, account3));
    List<Account> accounts = accountAddressService.getAccounts();

    assertThat(accounts, is(notNullValue()));
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
  public void createAccountReturnedAccountHasValuesTest() {
    when(accountRepository.save(any())).thenReturn(account1);
    Account account = callMethodWithJsonValue(CREATE, account1);

    assertThatAccountsAreEqual(account, account1);
  }

  @Test
  public void createAccountFailureTest() {
    Account account = callMethodWithJsonValue(CREATE, account1);

    assertThat(account, is(nullValue()));
  }

  @Test
  public void createAccountJacksonFailureTest() {
    Account account = accountAddressService.createAccount("{wrong format)");

    assertThat(account, is(nullValue()));
  }

  @Test
  public void updateAccountReturnedAccountHasValuesTest() {
    when(accountRepository.save(any())).thenReturn(account1);
    Account account = callMethodWithJsonValue(UPDATE, account1);

    assertThatAccountsAreEqual(account, account1);
  }

  @Test
  public void updateAccountRepositoryFailureTest() {
    Account account = callMethodWithJsonValue(UPDATE, account1);

    assertThat(account, is(nullValue()));

  }

  @Test
  public void updateAccountJacksonFailureTest() {
    Account account = accountAddressService.updateAccount(1, "{wrong format)");

    assertThat(account, is(nullValue()));
  }

  @Test
  public void deleteAccountReturnedAccountHasValuesTest() {
    when(accountRepository.findAccountById(any(Long.class))).thenReturn(account1);
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
    when(accountRepository.findAddressesById(any(Long.class))).thenReturn(Arrays.asList(
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

  private void assertThatAccountsAreEqual(Account account1, Account account2) {
    assertThat(account1, is(notNullValue()));
    assertThat(account1.getId(), is(equalTo(account2.getId())));
    assertThat(account1.getFirstName(), is(notNullValue()));
    assertThat(account1.getFirstName(), is(equalTo(account2.getFirstName())));
    assertThat(account1.getLastName(), is(notNullValue()));
    assertThat(account1.getLastName(), is(equalTo(account2.getLastName())));
    assertThat(account1.getEmail(), is(notNullValue()));
    assertThat(account1.getEmail(), is(equalTo(account2.getEmail())));
    assertThat(account1.getAddresses(), is(notNullValue()));
    assertThat(account1.getAddresses(), is(equalTo(account2.getAddresses())));
  }

  private Account callMethodWithJsonValue(String method, Account account) {
    Account result = null;
    try {
      switch(method) {
        case CREATE:
          result = accountAddressService.createAccount(objectMapper.writeValueAsString(account));
          break;
        case UPDATE:
          result = accountAddressService.updateAccount(1, objectMapper.writeValueAsString(account));
          break;
        default:
          logger.error("Unknown method '{}' given to callMethodWithJsonValue", method);
      }
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
    return result;
  }
}
