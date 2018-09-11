package com.solstice.accountaddress.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AccountAddressServiceUnitTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountAddressService accountAddressService;

  @Before
  public void setup() {
    Address address = new Address(
        "111 N Canal St",
        "700",
        "Chicago",
        "IL",
        "60606",
        "United States"
    );
    when(accountRepository.findAll()).thenReturn(Arrays.asList(
        new Account("Jane", "Doe", "jdoe@gmail.com", Arrays.asList(address)),
        new Account("John", "Smith", "jsmith@gmail.com", Arrays.asList(address)),
        new Account("Bill", "Murray", "bmurray@gmail.com", Arrays.asList(address))
    ));
  }

  @Test
  public void getAccountsNotEmptyTest() {
    List<Account> accounts = accountAddressService.getAccounts();

    assertThat(accounts, is(notNullValue()));
    assertFalse(accounts.isEmpty());
  }

  @Test
  public void getAccountsReturnedListHasValuesTest() {
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
}
