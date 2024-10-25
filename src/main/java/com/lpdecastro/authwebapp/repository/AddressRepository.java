package com.lpdecastro.authwebapp.repository;

import com.lpdecastro.authwebapp.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
