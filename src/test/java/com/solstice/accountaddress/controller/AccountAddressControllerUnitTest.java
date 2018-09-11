package com.solstice.accountaddress.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import com.solstice.accountaddress.service.AccountAddressService;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AccountAddressController.class)
public class AccountAddressControllerUnitTest {

  private Logger logger = LoggerFactory.getLogger(AccountAddressControllerUnitTest.class);
  private final String GET = "GET";
  private final String POST = "POST";
  private final String PUT = "PUT";
  private final String DELETE = "DELETE";

  @Mock
  private AccountAddressService accountAddressService;

  @InjectMocks
  private AccountAddressController accountAddressController;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(accountAddressController).build();
  }

  @Test
  public void getAccountsSuccessTest() {
    when(accountAddressService.getAccounts()).thenReturn(Arrays.asList(new Account()));
    mockMvcPerform(GET,"/accounts", 200);
  }

  @Test
  public void getAccountFailureTest() {
    mockMvcPerform(GET,"/accounts", 404);
  }

  @Test
  public void postAccountSuccessTest() {
    when(accountAddressService.createAccount(any(Long.class))).thenReturn(new Account());
    mockMvcPerform(POST,"/accounts/1", 201);
  }

  @Test
  public void postAccountFailureTest() {
    mockMvcPerform(POST,"/accounts/1", 500);
  }


  @Test
  public void putAccountSuccessTest() {
    when(accountAddressService.updateAccount(any(Long.class))).thenReturn(new Account());
    mockMvcPerform(PUT,"/accounts/1", 200);
  }

  @Test
  public void putAccountFailureTest() {
    mockMvcPerform(PUT,"/accounts/1", 500);
  }

  @Test
  public void deleteAccountSuccessTest() {
    when(accountAddressService.deleteAccount(any(Long.class))).thenReturn(new Account());
    mockMvcPerform(DELETE, "/accounts/1", 200);
  }

  @Test
  public void deleteAccountFailureTest() {
    mockMvcPerform(DELETE, "/accounts/1", 500);
  }

  @Test
  public void getAccountAddressSuccessTest() {
    when(accountAddressService.getAddressByAccountId()).thenReturn(Arrays.asList(new Address()));
    mockMvcPerform(GET,"/accounts/1/address", 200);
  }

  @Test
  public void getAddressFailureTest() {
    mockMvcPerform(GET,"/accounts/1/address", 404);
  }

  @Test
  public void postAddressSuccessTest() {
    when(accountAddressService.createAddress(any(Long.class))).thenReturn(Arrays.asList(new Address()));
    mockMvcPerform(POST,"/accounts/1/address", 201);
  }

  @Test
  public void postAddressFailureTest() {
    mockMvcPerform(POST,"/accounts/1/address", 500);
  }

  private void mockMvcPerform(String method, String endpoint, int expectedStatus) {
    try {
      switch(method){
        case GET:
          mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatus));
          break;
        case POST:
          mockMvc.perform(post(endpoint)).andExpect(status().is(expectedStatus));
          break;
        case PUT:
          mockMvc.perform(put(endpoint)).andExpect(status().is(expectedStatus));
          break;
        case DELETE:
          mockMvc.perform(delete(endpoint)).andExpect(status().is(expectedStatus));
          break;
        default:
          logger.error("Unknown method '{}' given to mockMvcPerform", method);
      }
    } catch (Exception e) {
      logger.error("Exception thrown: {}", e.toString());
    }
  }
}
