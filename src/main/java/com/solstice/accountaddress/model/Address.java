package com.solstice.accountaddress.model;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

@Entity
@SqlResultSetMapping(name="AddressInAccountMapping", classes = {
    @ConstructorResult(targetClass = Address.class,
        columns = {
            @ColumnResult(name="id", type=Long.class),
            @ColumnResult(name="street", type=String.class),
            @ColumnResult(name="apartment", type=String.class),
            @ColumnResult(name="city", type=String.class),
            @ColumnResult(name="state", type=String.class),
            @ColumnResult(name="zip", type=String.class),
            @ColumnResult(name="country", type=String.class)
        })
})
@NamedNativeQuery(
    name = "Address.findAddressByIdAndAccountId",
    query = "select id, street, apartment, city, state, zip, country "
        + "from address "
        + "where id = :addressId and :accountId = account_id",
    resultSetMapping = "AddressInAccountMapping"
)
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String street;
  private String apartment;
  private String city;
  private String state;
  private String zip;
  private String country;

  public Address(){

  }

  public Address(Long id, String street, String apartment, String city, String state, String zip,
      String country) {
    this.id = id;
    this.street = street;
    this.apartment = apartment;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.country = country;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getApartment() {
    return apartment;
  }

  public void setApartment(String apartment) {
    this.apartment = apartment;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}

