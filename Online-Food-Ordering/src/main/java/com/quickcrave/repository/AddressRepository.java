package com.quickcrave.repository;

import com.quickcrave.model.Address;
import com.quickcrave.model.Resturant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {

}
