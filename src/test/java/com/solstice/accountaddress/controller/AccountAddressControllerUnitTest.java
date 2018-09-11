package com.solstice.accountaddress.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import org.springframework.http.MediaType;
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
  private Account account;
  private Address address;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(accountAddressController).build();
    address = new Address(
        "111 N Canal St",
        "700",
        "Chicago",
        "IL",
        "60606",
        "United States"
    );
    account = new Account("Jane", "Doe", "jdoe@gmail.com", Arrays.asList(address));
  }

  @Test
  public void getAccountsSuccessTest() {
    when(accountAddressService.getAccounts()).thenReturn(Arrays.asList(new Account()));
    mockMvcPerform(GET,"/accounts", 200, "");
  }

  @Test
  public void getAccountFailureTest() {
    mockMvcPerform(GET,"/accounts", 404, "");
  }

  @Test
  public void postAccountSuccessTest() {
    when(accountAddressService.createAccount(any(String.class))).thenReturn(new Account());
    try {
      mockMvcPerform(POST,"/accounts", 201, objectMapper.writeValueAsString(account));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
  }

  @Test
  public void postAccountFailureTest() {
    try {
      mockMvcPerform(POST,"/accounts", 500, objectMapper.writeValueAsString(account));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
  }

  @Test
  public void postAccountEmptyBodyTest() {
    mockMvcPerform(POST,"/accounts", 400, "");
  }


  @Test
  public void putAccountSuccessTest() {
    when(accountAddressService.updateAccount(any(Long.class), any(String.class))).thenReturn(new Account());
    try {
      mockMvcPerform(PUT,"/accounts/1", 200, objectMapper.writeValueAsString(account));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
  }

  @Test
  public void putAccountFailureTest() {
    try {
      mockMvcPerform(PUT,"/accounts/1", 500, objectMapper.writeValueAsString(account));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
  }

  @Test
  public void putAccountEmptyBodyTest() {
    mockMvcPerform(PUT,"/accounts/1", 400, "");
  }

  @Test
  public void deleteAccountSuccessTest() {
    when(accountAddressService.deleteAccount(any(Long.class))).thenReturn(new Account());
    mockMvcPerform(DELETE, "/accounts/1", 200, "");
  }

  @Test
  public void deleteAccountFailureTest() {
    mockMvcPerform(DELETE, "/accounts/1", 500, "");
  }

  @Test
  public void getAccountAddressSuccessTest() {
    when(accountAddressService.getAddressesByAccountId(any(Long.class))).thenReturn(Arrays.asList(new Address()));
    mockMvcPerform(GET,"/accounts/1/address", 200, "");
  }

  @Test
  public void getAddressFailureTest() {
    mockMvcPerform(GET,"/accounts/1/address", 404, "");
  }

  @Test
  public void postAddressSuccessTest() {
    when(accountAddressService.createAddress(any(Long.class))).thenReturn(Arrays.asList(new Address()));
    try {
      mockMvcPerform(POST,"/accounts/1/address", 201, objectMapper.writeValueAsString(address));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
  }

  @Test
  public void postAddressFailureTest() {
    try {
      mockMvcPerform(POST,"/accounts/1/address", 500, objectMapper.writeValueAsString(address));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
  }

  private void mockMvcPerform(String method, String endpoint, int expectedStatus, String requestBody) {
    try {
      switch(method){
        case GET:
          mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatus));
          break;
        case POST:
          mockMvc.perform(
              post(endpoint)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody)
          ).andExpect(status().is(expectedStatus));
          break;
        case PUT:
          mockMvc.perform(
              put(endpoint)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody)
          ).andExpect(status().is(expectedStatus));
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
