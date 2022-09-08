package com.example.demo.phonenumber.repository;

import com.example.demo.phonenumber.jpamodel.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    @Query("SELECT pn FROM PhoneNumbers pn WHERE pn.prefix = :prefix and pn.phoneNumber = :number")
    PhoneNumber matchPhoneNumber(String prefix, String number);
}
