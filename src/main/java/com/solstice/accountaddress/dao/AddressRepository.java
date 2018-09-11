package com.solstice.accountaddress.dao;

import com.solstice.accountaddress.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
