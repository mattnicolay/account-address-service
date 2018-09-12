package com.solstice.accountaddress.dao;

import com.solstice.accountaddress.model.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends CrudRepository<Address, Long> {

  @Query(nativeQuery = true)
  Address findAddressByIdAndAccountId(@Param("addressId") long id, @Param("accountId") long accountId);
}
