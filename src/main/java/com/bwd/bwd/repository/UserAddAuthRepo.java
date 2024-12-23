package com.bwd.bwd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bwd.bwd.request.AddressData;

public interface UserAddAuthRepo extends JpaRepository<AddressData, Long> {

}
