package com.solstice.accountaddress.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.accountaddress.model.Account;
import com.solstice.accountaddress.model.Address;
import com.solstice.accountaddress.service.AccountAddressService;
import java.io.IOException;
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
    mockMvcPerform(GET,"/accounts", 200, "", toJson(Arrays.asList(new Account())));
  }

  @Test
  public void getAccountsFailureTest() {
    mockMvcPerform(GET,"/accounts", 404, "", "");
  }

  @Test
  public void getAccountByIdSuccessTest() {
    when(accountAddressService.getAccountById(anyLong())).thenReturn(account);
    mockMvcPerform(GET, "/accounts/1", 200, "", toJson(account));
  }

  @Test
  public void getAccountByIdFailureTest() {
    mockMvcPerform(GET, "/accounts/1", 500, "", "");
  }

  @Test
  public void postAccountSuccessTest() throws IOException {
    when(accountAddressService.createAccount(anyString())).thenReturn(account);
    mockMvcPerform(POST,"/accounts", 201, toJson(account), toJson(account));
  }

  @Test
  public void postAccountFailureTest() {
    mockMvcPerform(POST,"/accounts", 500, toJson(account), "");
  }

  @Test
  public void postAccountEmptyBodyTest() {
    mockMvcPerform(POST,"/accounts", 400, "", "");
  }


  @Test
  public void putAccount_ValidIdAndJson_Code200ReturnsJson() throws IOException {
    when(accountAddressService.updateAccount(anyLong(), anyString())).thenReturn(account);
    mockMvcPerform(PUT,"/accounts/1", 200, toJson(account), toJson(account));
  }

  @Test
  public void putAccount_NotFound_Code404() {
    mockMvcPerform(PUT,"/accounts/1", 404, toJson(account), "");
  }

  @Test
  public void putAccount_EmptyBody_Code400() {
    mockMvcPerform(PUT,"/accounts/1", 400, "", "");
  }

  @Test
  public void putAccount_InvalidJson_Code400() throws IOException {
    when(accountAddressService.updateAccount(1, "{wrong}")).thenThrow(new IOException());
    mockMvcPerform(PUT,"/accounts/1",  400, "{wrong}", "");
  }

  @Test
  public void deleteAccountSuccessTest() {
    when(accountAddressService.deleteAccount(anyLong())).thenReturn(new Account());
    mockMvcPerform(DELETE, "/accounts/1", 200, "", toJson(new Account()));
  }

  @Test
  public void deleteAccountFailureTest() {
    mockMvcPerform(DELETE, "/accounts/1", 500, "", "");
  }

  @Test
  public void getAccountAddressSuccessTest() {
    when(accountAddressService.getAddressesByAccountId(anyLong())).thenReturn(Arrays.asList(new Address()));
    mockMvcPerform(GET,"/accounts/1/address", 200, "", toJson(Arrays.asList(new Address())));
  }

  @Test
  public void getAddressFailureTest() {
    mockMvcPerform(GET,"/accounts/1/address", 404, "", "");
  }

  @Test
  public void getAddressByIdSuccessTest() {
    when(accountAddressService.getAddressByAccountIdAndAddressId(anyLong(), anyLong())).thenReturn(address);
    mockMvcPerform(GET, "/accounts/1/address/1", 200, "", toJson(address));
  }

  @Test
  public void getAddressByIdNotFoundTest() {
    mockMvcPerform(GET, "/accounts/1/address/1", 404, "", "");
  }

  @Test
  public void postAddressSuccessTest() throws IOException {
    when(accountAddressService.createAddress(anyLong(), anyString())).thenReturn(new Address());
    mockMvcPerform(POST,"/accounts/1/address", 201, toJson(address), toJson(new Address()));
  }

  @Test
  public void postAddressFailureTest() {
    mockMvcPerform(POST,"/accounts/1/address", 500, toJson(address), "");
  }

  @Test
  public void postAddressEmptyBodyTest() {
    mockMvcPerform(POST,"/accounts/1/address", 400, "", "");
  }

  @Test
  public void putAddressSuccessTest() throws IOException {
    when(accountAddressService.updateAddress(anyLong(), anyLong(), anyString())).thenReturn(address);
    mockMvcPerform(PUT, "/accounts/1/address/1", 200, toJson(address), toJson(address));
  }

  @Test
  public void putAddressNotFoundTest() {
    mockMvcPerform(PUT, "/accounts/1/address/1", 404, toJson(address), "");
  }

  @Test
  public void putAddressEmptyBodyTest() {
    mockMvcPerform(PUT, "/accounts/1/address/1", 400, "", "");
  }

  @Test
  public void deleteAddressSuccessTest() {
    when(accountAddressService.deleteAddress(anyLong(), anyLong())).thenReturn(address);
    mockMvcPerform(DELETE, "/accounts/1/address/1", 200, "", toJson(address));
  }

  @Test
  public void deleteAddressNotFoundTest() {
    mockMvcPerform(DELETE, "/accounts/1/address/1", 404, "", "");
  }



  private String toJson(Object value) {
    String result = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      result = objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
    return result;
  }

  private void mockMvcPerform(String method, String endpoint, int expectedStatus, String requestBody,
      String expectedResponseBody) {
    try {
      switch(method){

        case GET:
          mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatus))
              .andExpect(content().json(expectedResponseBody));
          break;

        case POST:
          mockMvc.perform(
              post(endpoint)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody)
          ).andExpect(status().is(expectedStatus))
              .andExpect(content().json(expectedResponseBody));
          break;

        case PUT:
          mockMvc.perform(
              put(endpoint)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody)
          ).andExpect(status().is(expectedStatus))
              .andExpect(content().json(expectedResponseBody));
          break;

        case DELETE:
          mockMvc.perform(delete(endpoint)).andExpect(status().is(expectedStatus))
              .andExpect(content().json(expectedResponseBody));
          break;

        default:
          logger.error("Unknown method '{}' given to mockMvcPerform", method);
      }
    } catch (Exception e) {
      logger.error("Exception thrown: {}", e.toString());
    }
  }
}
