package com.restspringtest.Serviços;

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
import com.restspringtest.Services.PersonService;

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
    void testGivenPersonExistEmail_whenSave_ThenThrowsException() {
        // given / arrange
        given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person0));

        // when / act
        assertThrows(RuntimeException.class, () -> personService.save(person0));

        // then / assert
        verify(personRepository, never()).save(any());
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
    void testGivenPersonId_whenUpdate_ThenReturnUpdatedPerson() {
        // given / arrange
        Person person1 = new Person("John2", "Doe2", "Street2", "M2", "john@email.com");
        person1.setId(1L); // Certifique-se de que o ID está correto e configurado

        given(personRepository.existsById(1L)).willReturn(true);

        doNothing().when(personRepository).updatePerson(
                "John2", "Doe2", "Street2", "M2", "john@email.com", 1L);

        // when / act
        Person updatedPerson = personService.update(person1);

        // then / assert
        assertEquals("John2", updatedPerson.getFirstName());
        assertEquals(person1.getEmail(), updatedPerson.getEmail());
        verify(personRepository, times(1)).updatePerson(
                "John2", "Doe2", "Street2", "M2", "john@email.com", 1L);
    }

    @Test
    void testGivenPersonId_whenDeleteById_thenPersonDeletedSuccessfully() {
        // given / arrange
        Long personId = person0.getId();

        given(personRepository.existsById(personId)).willReturn(true);
        doNothing().when(personRepository).deleteById(personId);

        // when / act
        String result = personService.deleteById(personId);

        // then / assert
        assertEquals("Person deleted successfully", result);
        verify(personRepository, times(1)).deleteById(personId);
    }

}
