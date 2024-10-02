package com.restspringtest.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restspringtest.Model.Person;
import com.restspringtest.Repository.PersonRepository;
import com.restspringtest.Services.Exception.ExceptionBusinessRules;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = true)
    public Person findById(Long id) {
        Person client = personRepository.findById(id)
                .orElseThrow(() -> new ExceptionBusinessRules("Client not found, id does not exist: " + id));
        return client;
    }

    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable page) {
        Page<Person> client = personRepository.findAll(page);
        return client;
    }

    @Transactional
    public Person save(Person person) {
        personRepository.save(person);
        return person;
    }

    @Transactional
    public String deleteById(Long id) {
        try {
            if (personRepository.existsById(id) == true)
                personRepository.deleteById(id);
            return "Person deleted successfully";
        } catch (Exception e) {
            throw new ExceptionBusinessRules("Person not found, id does not exist: " + id);
        }

    }

    @Transactional
    public Person update(Person person) {
        try {
            if (personRepository.existsById(person.getId()) == true)
                personRepository.updatePerson(person.getFirstName(), person.getLastName(), person.getAddress(),
                        person.getGender(), person.getId());
            return person;
        } catch (Exception e) {
            throw new ExceptionBusinessRules("Person not found, id does not exist: " + person.getId());
        }
    }

}
