package com.solstice.accountaddress.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.solstice.accountaddress.model.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:test-dataset.xml")
public class AddressRepositoryUnitTests {

  @Autowired
  private AddressRepository addressRepository;

  @Test
  public void findAddressByIdAndAddressIdTest() {
    Address address = addressRepository.findAddressByIdAndAccountId(3, 3);
    assertThat(address, is(notNullValue()));
    assertThat(address.getStreet(), is(equalTo("405 Lexington Ave")));
    assertThat(address.getApartment(), is(equalTo("512")));
    assertThat(address.getCity(), is(equalTo("New York")));
    assertThat(address.getState(), is(equalTo("NY")));
    assertThat(address.getZip(), is(equalTo("10174")));
    assertThat(address.getCountry(), is(equalTo("United States")));
  }
}
