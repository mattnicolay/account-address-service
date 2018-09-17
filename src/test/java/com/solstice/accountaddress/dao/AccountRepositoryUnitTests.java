package com.solstice.accountaddress.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.solstice.accountaddress.model.Address;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:test-dataset.xml")
public class AccountRepositoryUnitTests {

  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void findAddressesByIdTest() {
    List<Address> addresses = accountRepository.findAddressesByAccountId(1);
    assertThat(addresses, is(notNullValue()));
    assertThat(addresses.size(), is(equalTo(2)));
    Address address1 = addresses.get(0);
    assertThat(address1.getStreet(), is(equalTo("111 N Canal St")));
    assertThat(address1.getApartment(), is(equalTo("700")));
    assertThat(address1.getCity(), is(equalTo("Chicago")));
    assertThat(address1.getState(), is(equalTo("IL")));
    assertThat(address1.getZip(), is(equalTo("60606")));
    assertThat(address1.getCountry(), is(equalTo("United States")));

    Address address2 = addresses.get(1);
    assertThat(address2.getStreet(), is(equalTo("405 Lexington Ave")));
    assertThat(address2.getApartment(), is(equalTo("6502")));
    assertThat(address2.getCity(), is(equalTo("New York")));
    assertThat(address2.getState(), is(equalTo("NY")));
    assertThat(address2.getZip(), is(equalTo("10174")));
    assertThat(address2.getCountry(), is(equalTo("United States")));
  }
}
