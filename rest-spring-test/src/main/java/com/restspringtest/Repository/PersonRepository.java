package com.restspringtest.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restspringtest.Model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    
}
