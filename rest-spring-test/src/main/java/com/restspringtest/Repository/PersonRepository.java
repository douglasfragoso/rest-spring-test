package com.restspringtest.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.restspringtest.Model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
   
    @Transactional
    @Modifying
    @Query(value = "UPDATE tb_person SET first_name = :firstName, last_name = :lastName, address = :address, gender = :gender WHERE id = :id", nativeQuery = true)
    void updatePerson(String firstName, String lastName, String address, String gender, Long id);
}
