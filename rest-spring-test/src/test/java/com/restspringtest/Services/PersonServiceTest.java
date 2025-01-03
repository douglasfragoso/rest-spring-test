package com.restspringtest.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.restspringtest.Model.Person;
import com.restspringtest.Repository.PersonRepository;
import com.restspringtest.Services.Exception.DatabaseException;

import jakarta.persistence.EntityManager;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

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
        Pageable pageable = PageRequest.of(0, 10);
        Page<Person> personPage = new PageImpl<>(List.of(person0, person1), pageable, 2);

        given(personRepository.findAll(pageable)).willReturn(personPage);

        // when / act
        Page<Person> personList = personService.findAll(pageable);

        // then / assert
        assertNotNull(personList);
        assertEquals(2, personList.getTotalElements());
    }

    @Test
    void testGivenPersonList_whenFindAll_ThenReturnEmptyPersonList() {
        // given / arrange
        Pageable pageable = PageRequest.of(0, 10);

        given(personRepository.findAll(pageable)).willReturn(Page.empty(pageable));

        // when / act
        Page<Person> personList = personService.findAll(pageable);

        // then / assert
        assertTrue(personList.isEmpty());
        assertEquals(0, personList.getTotalElements());
    }

    @Test
    void testGivenPersonId_whenFindById_ThenReturPerson() {
        // given / arrange
        given(personRepository.findById(anyLong())).willReturn(Optional.of(person0));

        // when / act
        Person savedPerson = personService.findById(1L);

        // then / assert
        assertNotNull(savedPerson);
        assertEquals("John", savedPerson.getFirstName());
    }

    @Test
    void testGivenId_whenFindById_ThenThrowsException() {
        // given / arrange
        given(personRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / act & then / assert
        Exception exception = assertThrows(DatabaseException.class,
                () -> personService.findById(1L));

        // Verifica se a mensagem da exceção está correta
        assertEquals("Person not found, id: 1", exception.getMessage());
    }

    @Test
    void testGivenPersonExistEmail_whenSave_ThenThrowsException() {
        // given / arrange
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person0));

        // when / act
        assertThrows(DatabaseException.class, () -> personService.save(person0));

        // then / assert
        verify(personRepository, never()).save(any());
    }

    @Test
    void testGivenPersonExistEmail_whenSave_ThenReturPerson() {
        // given / arrange
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person0));

        // when / act
        Person savedPerson = personService.findByEmail("jhon@email.com");

        // then / assert
        assertNotNull(savedPerson);
        assertEquals("John", savedPerson.getFirstName());
    }

    @Test
    void testGivenEmail_whenFindByEmail_ThenThrowsException() {
        // given / arrange
        given(personRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when / act & then / assert
        Exception exception = assertThrows(DatabaseException.class,
                () -> personService.findByEmail("unknown@email.com"));

        // Verifica se a mensagem da exceção está correta
        assertEquals("Person not found, email: unknown@email.com", exception.getMessage());
    }

    @Test
    void testGivenPersonObject_whenSave_ThenReturPerson() {
        // given / arrange
        given(personRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(personRepository.save(person0)).willReturn(person0);

        // when / act
        Person savedPerson = personService.save(person0);

        // then / assert
        assertNotNull(savedPerson);
        assertEquals("John", savedPerson.getFirstName());
    }

    @Test
    void testGivenPersonId_whenUpdate_thenReturnNothing() {
        // given / arrange
        Person person1 = new Person("John2", "Doe2", "Street2", "M2", "john@email.com");
        person1.setId(1L);

        given(personRepository.findById(1L)).willReturn(Optional.of(person1)); 

        doNothing().when(personRepository).updatePerson(
                1L, "John2", "Doe2", "Street2", "M2", "john@email.com");

        // when / act
        personService.update(person1); 

        // Assert
        verify(personRepository, times(1)).updatePerson(1L, "John2", "Doe2", "Street2", "M2", "john@email.com");
    }

    @Test
    void testGivenNonExistingPerson_whenUpdate_ThenThrowsException() {
        // given / arrange
        Person person1 = new Person("John2", "Doe2", "Street2", "M2", "john@email.com");
        person1.setId(1L);

        given(personRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / act & then / assert
        Exception exception = assertThrows(DatabaseException.class, () -> personService.update(person1));

        assertEquals("Person not found, id: " + person1.getId(), exception.getMessage());
    }

    @Test
    void testGivenPersonId_whenDeleteById_thenReturnNothing() {
        // given / arrange
        Long personId = 1L;

        given(personRepository.findById(anyLong())).willReturn(Optional.of(person0));
        doNothing().when(personRepository).deleteById(personId);

        // when / act
        personService.deleteById(personId);

        // then / assert
        verify(personRepository, times(1)).deleteById(personId);
    }

    @Test
    void testGivenNonExistingPerson_whenDeleteById_ThenThrowsException() {
        // given / arrange
        Long personId = 1L;
        given(personRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / act & then / assert
        Exception exception = assertThrows(DatabaseException.class, () -> personService.deleteById(personId));

        // Verifica se a mensagem da exceção está correta
        assertEquals("Person not found, id: " + personId, exception.getMessage());
    }

}
