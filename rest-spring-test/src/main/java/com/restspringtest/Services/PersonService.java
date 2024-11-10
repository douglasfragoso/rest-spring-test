package com.restspringtest.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restspringtest.Model.Person;
import com.restspringtest.Repository.PersonRepository;
import com.restspringtest.Services.Exception.DatabaseException;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = true)
    public Person findById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new DatabaseException("Person not found, id: " + id));
        return person;
    }

    @Transactional(readOnly = true)
    public Person findByEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new DatabaseException("Person not found, email: " + email));
        return person;
    }

    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable page) {
        Page<Person> client = personRepository.findAll(page);
        return client;
    }

    @Transactional
    public Person save(Person person) {
        Optional<Person> savedPerson = personRepository.findByEmail(person.getEmail());
        if (savedPerson.isPresent()) {
            throw new DatabaseException("Email already registered: " + person.getEmail());
        }
        personRepository.save(person);
        return person;
    }

    @Transactional
    public void update(Person person) {
        if (!personRepository.findById(person.getId()).isPresent()) {
            throw new DatabaseException("Person not found, id: " + person.getId());
        }
        personRepository.updatePerson(person.getId(), person.getFirstName(), person.getLastName(),
                person.getAddress(), person.getGender(), person.getEmail());
    }

    @Transactional
    public void deleteById(Long id) {
        personRepository.findById(id)
                .orElseThrow(() -> new DatabaseException("Person not found, id: " + id));
        personRepository.deleteById(id);
    }

}
