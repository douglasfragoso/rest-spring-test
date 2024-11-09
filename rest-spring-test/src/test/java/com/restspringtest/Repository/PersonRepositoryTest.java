package com.restspringtest.Repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.restspringtest.Integration.TestContainers.AbstractIntegrationTest;
import com.restspringtest.Model.Person;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    private Person person0;

    @BeforeEach
    // given / arrange
    public void setUp() {
        person0 = new Person("John", "Doe", "Street", "M", "john@email.com");
    }

    @Test
    void testGivenPersonList_whenFindAll_ThenReturnPersonList() {
        // given / arrange
        Person person1 = new Person("John1", "Doe1", "Street1", "M", "john1@email.com");

        personRepository.save(person0);
        personRepository.save(person1);

        // when / act
        List<Person> personList = personRepository.findAll();

        // then / assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @Test
    void testGivenPersonList_whenFindById_ThenReturnPerson() {
        // given / arrange
        personRepository.save(person0);

        // when / act
        Person person1 = personRepository.findById(person0.getId()).orElseThrow();

        // then / assert
        assertNotNull(person1);
        assertEquals(person0.getId(), person1.getId());
    }

    @Test
    void testGivenPersonList_whenFindById_ThenThrowsException() {
        // then / assert
        assertThrows(NoSuchElementException.class, () -> {
            personRepository.findById(99L).orElseThrow();
        });
    }

    @Test
    void testGivenPersonList_whenFindByEmail_ThenReturnPerson() {
        // given / arrange
        personRepository.save(person0);

        // when / act
        Person person1 = personRepository.findByEmail(person0.getEmail()).orElseThrow();

        // then / assert
        assertNotNull(person1);
        assertEquals(person0.getEmail(), person1.getEmail());
    }

    @Test
    void testGivenPersonList_whenFindByEmail_ThenThrowsException() {
        // then / assert
        assertThrows(NoSuchElementException.class, () -> {
            personRepository.findByEmail("nonexistent@email.com").orElseThrow();
        });
    }

    @Test
    void testGivenPersonObject_whenSave_ThenReturPerson() {
        // when / act
        Person savedPerson = personRepository.save(person0);

        // then / assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    @Transactional
    void testGivenPersonList_whenUpdate_ThenReturnPerson() {
        // given / arrange
        personRepository.save(person0);

        // when / act
        personRepository.updatePerson(person0.getId(), "John2", "Doe2", "Street2", "M", "john1@email.com");

        entityManager.clear();

        Person updatedPerson = personRepository.findById(person0.getId()).orElseThrow();

        // then / assert
        assertNotNull(updatedPerson);
        assertEquals("John2", updatedPerson.getFirstName());
        assertEquals("Street2", updatedPerson.getAddress());
        assertEquals("M", updatedPerson.getGender());
        assertEquals("john1@email.com", updatedPerson.getEmail());
    }

    @Test
    @Transactional
    void testGivenNonExistentId_whenUpdatePerson_thenNoUpdateHappens() {
        // given / arrange
        personRepository.save(person0);

        Long nonExistentId = 999L;

        // when / act
        personRepository.updatePerson(nonExistentId, "John2", "Doe2", "Street2", "M", "john1@email.com");

        entityManager.clear();

        Person originalPerson = personRepository.findById(person0.getId()).orElseThrow();

        // then / assert
        assertNotNull(originalPerson);
        assertEquals("John", originalPerson.getFirstName()); 
        assertEquals("Doe", originalPerson.getLastName());
        assertEquals("Street", originalPerson.getAddress());
        assertEquals("M", originalPerson.getGender());
        assertEquals("john@email.com", originalPerson.getEmail());
    }

    @Test
    @Transactional
    void testGivenPersonList_whenDeleteById_ThenReturnPerson() {
        // given / arrange
        personRepository.save(person0);

        // when / act
        personRepository.deleteById(person0.getId());
        Optional<Person> person1 = personRepository.findById(person0.getId());

        // then / assert
        assertTrue(person1.isEmpty());
    }

}
