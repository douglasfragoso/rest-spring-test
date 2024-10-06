package com.restspringtest.Repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.restspringtest.Model.Person;

import jakarta.persistence.EntityManager;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testGivenPersonObject_whenSave_ThenReturPerson() {
        //given / arrange
        Person  person = new Person("John", "Doe", "Street", "M", "john@email.com");

        //when / act
        Person savedPerson = personRepository.save(person);

        //then / assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    void testGivenPersonList_whenFindAll_ThenReturnPersonList() {
        //given / arrange
        Person  person1 = new Person("John", "Doe", "Street", "M", "john@email.com");
        Person  person2 = new Person("John2", "Doe2", "Street2", "M", "john2@email.com");

        personRepository.save(person1);
        personRepository.save(person2);
        //when / act
       
        List<Person> personList = personRepository.findAll();

        //then / assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @Test
    void testGivenPersonList_whenFindById_ThenReturnPersonList() {
        //given / arrange
        Person  person1 = new Person("John", "Doe", "Street", "M", "john@email.com");

        personRepository.save(person1);
        //when / act
       
        Person person0 = personRepository.findById(person1.getId()).orElseThrow();

        //then / assert
        assertNotNull(person0);
        assertEquals(person1.getId(), person0.getId());
    }

    @Test
    void testGivenPersonList_whenFindByEmail_ThenReturnPerson() {
        //given / arrange
        Person  person1 = new Person("John", "Doe", "Street", "M", "john@email.com");

        personRepository.save(person1);
        //when / act
       
        Person person0 = personRepository.findByEmail(person1.getEmail()).orElseThrow();

        //then / assert
        assertNotNull(person0);
        assertEquals(person1.getEmail(), person0.getEmail());
    }
    
    @Test
    @Transactional
    void testGivenPersonList_whenUpdate_ThenReturnPerson() {
        //given / arrange
        Person person1 = new Person("John", "Doe", "Street", "M", "john@email.com");
    
        personRepository.save(person1);
        
        //when / act
        personRepository.updatePerson("John2", "Doe2", "Street2", "M", "john@email.com", person1.getId());
    
        entityManager.clear();

        Person updatedPerson = personRepository.findById(person1.getId()).orElseThrow();
    
        //then / assert
        assertNotNull(updatedPerson);
        assertEquals("John2", updatedPerson.getFirstName());
        assertEquals("Street2", updatedPerson.getAddress());
    }

    @Test
    void testGivenPersonList_whenDeleteById_ThenReturnPerson() {
        //given / arrange
        Person  person1 = new Person("John", "Doe", "Street", "M", "john@email.com");

        personRepository.save(person1);
        //when / act
       
        personRepository.deleteById(person1.getId());
        Optional<Person> person0 = personRepository.findById(person1.getId());

        //then / assert
        assertTrue(person0.isEmpty());
    }

}

