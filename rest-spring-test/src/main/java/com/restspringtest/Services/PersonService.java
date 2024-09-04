package com.restspringtest.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restspringtest.Model.Person;
import com.restspringtest.Repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = true)
    public Optional<Person> findById(Long id) {
        Optional<Person> client = personRepository.findById(id);
        return client;
    }

    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable page) {
        Page<Person> client = personRepository.findAll(page);
        return client;
    }

}
