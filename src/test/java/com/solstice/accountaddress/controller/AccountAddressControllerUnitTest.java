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
import com.solstice.accountaddress.exception.AccountAddressExceptionHandler;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringRunner.class)
@WebMvcTest(AccountAddressController.class)
public class AccountAddressControllerUnitTest {

  private Logger logger = LoggerFactory.getLogger(AccountAddressControllerUnitTest.class);
  private final String GET = "GET";
  private final String POST = "POST";
  private final String PUT = "PUT";
  private final String DELETE = "DELETE";
  private final String WRONG_JSON_FORMAT = "{wrong}";

  @MockBean
  private AccountAddressService accountAddressService;

  private MockMvc mockMvc;
  private Account account;
  private Address address;

  @Before
  public void setup() {
    AccountAddressController accountAddressController =
        new AccountAddressController(accountAddressService);
    mockMvc = MockMvcBuilders.standaloneSetup(accountAddressController)
        .setControllerAdvice(new AccountAddressExceptionHandler())
        .build();
    address = new Address(
        1L,
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
  public void getAccounts_Found_Code200ReturnsListOfAccounts() throws Exception {
    when(accountAddressService.getAccounts()).thenReturn(Arrays.asList(new Account()));
    mockMvcPerform(GET,"/accounts", 200, "", toJson(Arrays.asList(new Account())));
  }

  @Test
  public void getAccounts_NotFound_Code404EmptyBody() throws Exception {
    mockMvcPerform(GET,"/accounts", 404, "", "[]");
  }

  @Test
  public void getAccountById_ValidId_Code200ReturnsAccount() throws Exception {
    when(accountAddressService.getAccountById(anyLong())).thenReturn(account);
    mockMvcPerform(GET, "/accounts/1", 200, "", toJson(account));
  }

  @Test
  public void getAccountById_InvalidId_Code404EmptyBody() throws Exception {
    mockMvcPerform(GET, "/accounts/1", 404, "", "");
  }

  @Test
  public void postAccount_ValidJson_Code200ReturnsAccount() throws Exception {
    when(accountAddressService.createAccount(anyString())).thenReturn(account);
    mockMvcPerform(POST,"/accounts", 201, toJson(account), toJson(account));
  }

  @Test
  public void postAccount_InternalError_Code500() throws Exception {
    mockMvcPerform(POST,"/accounts", 500, toJson(account), "");
  }

  @Test
  public void postAccount_InvalidJson_Code400() throws Exception {
    when(accountAddressService.createAccount(WRONG_JSON_FORMAT)).thenThrow(new IOException());
    mockMvcPerform(POST,"/accounts", 400, WRONG_JSON_FORMAT,
        "<h1>ERROR:</h1>\n"
        + " Invalid Json format");
  }

  @Test
  public void postAccount_EmptyBody_Code400() throws Exception {
    mockMvcPerform(POST,"/accounts", 400, "", "");
  }


  @Test
  public void putAccount_ValidIdAndJson_Code200ReturnsJson() throws Exception {
    when(accountAddressService.updateAccount(anyLong(), anyString())).thenReturn(account);
    mockMvcPerform(PUT,"/accounts/1", 200, toJson(account), toJson(account));
  }

  @Test
  public void putAccount_NotFound_Code404() throws Exception {
    mockMvcPerform(PUT,"/accounts/1", 404, toJson(account), "");
  }

  @Test
  public void putAccount_EmptyBody_Code400() throws Exception {
    mockMvcPerform(PUT,"/accounts/1", 400, "", "");
  }

  @Test
  public void putAccount_InvalidJson_Code400() throws Exception {
    when(accountAddressService.updateAccount(1, WRONG_JSON_FORMAT)).thenThrow(new IOException());
    mockMvcPerform(PUT,"/accounts/1",  400, WRONG_JSON_FORMAT,
        "<h1>ERROR:</h1>\n"
            + " Invalid Json format");
  }

  @Test
  public void deleteAccount_Found_Code200ReturnsDeletedAccount() throws Exception {
    when(accountAddressService.deleteAccount(anyLong())).thenReturn(new Account());
    mockMvcPerform(DELETE, "/accounts/1", 200, "", toJson(new Account()));
  }

  @Test
  public void deleteAccount_NotFound_Code200ReturnsDeletedAccount() throws Exception {
    mockMvcPerform(DELETE, "/accounts/1", 404, "", "");
  }

  @Test
  public void getAddresses_Found_Code200ReturnsListOfAddresses() throws Exception {
    when(accountAddressService.getAddressesByAccountId(anyLong())).thenReturn(Arrays.asList(new Address()));
    mockMvcPerform(GET,"/accounts/1/address", 200, "", toJson(Arrays.asList(new Address())));
  }

  @Test
  public void getAddresses_NotFound_Code404ReturnsEmptyListOfAddresses() throws Exception {
    mockMvcPerform(GET,"/accounts/1/address", 404, "", "[]");
  }

  @Test
  public void getAddressById_ValidId_Code200ReturnsAddress() throws Exception {
    when(accountAddressService.getAddressByAccountIdAndAddressId(anyLong(), anyLong())).thenReturn(address);
    mockMvcPerform(GET, "/accounts/1/address/1", 200, "", toJson(address));
  }

  @Test
  public void getAddressById_InvalidId_Code404EmptyBody() throws Exception {
    mockMvcPerform(GET, "/accounts/1/address/-1", 404, "", "");
  }

  @Test
  public void postAddress_ValidJson_Code201ReturnsAddress() throws Exception {
    when(accountAddressService.createAddress(anyLong(), anyString())).thenReturn(new Address());
    mockMvcPerform(POST,"/accounts/1/address", 201, toJson(address), toJson(new Address()));
  }

  @Test
  public void postAddress_InternalError_Code500EmptyBody() throws Exception {
    mockMvcPerform(POST,"/accounts/1/address", 500, toJson(address), "");
  }

  @Test
  public void postAddress_EmptyRequestBody_Code400() throws Exception {
    mockMvcPerform(POST,"/accounts/1/address", 400, "", "");
  }

  @Test
  public void postAddress_InvalidJson_Code400() throws Exception {
    when(accountAddressService.createAddress(1, WRONG_JSON_FORMAT)).thenThrow(new IOException());
    mockMvcPerform(POST,"/accounts/1/address", 400, WRONG_JSON_FORMAT,
        "<h1>ERROR:</h1>\n"
            + " Invalid Json format");
  }

  @Test
  public void putAddress_ValidIdAndJson_Code200ReturnsAddress() throws Exception {
    when(accountAddressService.updateAddress(anyLong(), anyLong(), anyString())).thenReturn(address);
    mockMvcPerform(PUT, "/accounts/1/address/1", 200, toJson(address), toJson(address));
  }

  @Test
  public void putAddress_InvalidId_Code404EmptyBody() throws Exception {
    mockMvcPerform(PUT, "/accounts/1/address/-1", 404, toJson(address), "");
  }

  @Test
  public void putAddress_EmptyRequestBody_Code400() throws Exception {
    mockMvcPerform(PUT, "/accounts/1/address/1", 400, "", "");
  }

  @Test
  public void putAddress_InvalidJson_Code400() throws Exception {
    when(accountAddressService.updateAddress(1,1, WRONG_JSON_FORMAT)).thenThrow(new IOException());
    mockMvcPerform(PUT,"/accounts/1/address/1", 400, WRONG_JSON_FORMAT,
        "<h1>ERROR:</h1>\n"
            + " Invalid Json format");
  }

  @Test
  public void deleteAddress_ValidId_Code200ReturnsAddress() throws Exception {
    when(accountAddressService.deleteAddress(anyLong(), anyLong())).thenReturn(address);
    mockMvcPerform(DELETE, "/accounts/1/address/1", 200, "", toJson(address));
  }

  @Test
  public void deleteAddress_InvalidId_Code404EmptyBody() throws Exception {
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
      String expectedResponseBody) throws Exception {
    switch(method){

      case GET:
        mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      case POST:
        mockMvc.perform(
            post(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      case PUT:
        mockMvc.perform(
            put(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      case DELETE:
        mockMvc.perform(delete(endpoint)).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      default:
        logger.error("Unknown method '{}' given to mockMvcPerform", method);
    }
  }
}
