package com.restspringtest.Controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restspringtest.Model.Person;
import com.restspringtest.Services.PersonService;
import com.restspringtest.Services.Exception.DatabaseException;

@WebMvcTest
public class PersonControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private PersonService personService;

        private Person person;

        @BeforeEach
        // given / arrange
        public void setUp() {
                person = new Person("John", "Doe", "Street", "M", "john@email.com");
        }

        @Test
        void testListPersonObject_whenFindAlll_ThenReturnListPerson() throws JsonProcessingException, Exception {
                // given / arrange
                Person person2 = new Person("John1", "Doe1", "Street1", "M", "john1@email.com");
                Pageable pageable = PageRequest.of(0, 10);
                Page<Person> personPage = new PageImpl<>(List.of(person, person2), pageable, 2);

                given(personService.findAll(any(Pageable.class))).willReturn(personPage);

                // when / act
                ResultActions response = mockMvc.perform(get("/person")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType("application/json"));

                // then / assert
                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.totalElements").value(personPage.getTotalElements()));
        }

        @Test
        void testGivenPersonId_whenFindById_ThenReturnPerson() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.findById(id)).willReturn(person);

                // when / act
                ResultActions response = mockMvc.perform(get("/person/id/{id}", id));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                                .andExpect(jsonPath("$.address").value(person.getAddress()))
                                .andExpect(jsonPath("$.gender").value(person.getGender()))
                                .andExpect(jsonPath("$.email").value(person.getEmail()));
        }

        @Test
        void testGivenPersonId_whenFindById_ThenReturnNotFound() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.findById(id)).willThrow(DatabaseException.class);

                // when / act
                ResultActions response = mockMvc.perform(get("/id/{id}", id));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNotFound());
        }

        @Test
        void testGivenPersonId_whenFindByEmail_ThenReturnPerson() throws JsonProcessingException, Exception {
                // given / arrange
                String email = "john@email.com";
                given(personService.findByEmail(email)).willReturn(person);

                // when / act
                ResultActions response = mockMvc.perform(get("/person/email/{email}", email));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                                .andExpect(jsonPath("$.address").value(person.getAddress()))
                                .andExpect(jsonPath("$.gender").value(person.getGender()))
                                .andExpect(jsonPath("$.email").value(person.getEmail()));
        }

        @Test
        void testGivenPersonId_whenFindByEmail_ThenReturnNotFound() throws JsonProcessingException, Exception {
                // given / arrange
                String email = "john@email.com";
                given(personService.findByEmail(email)).willThrow(DatabaseException.class);

                // when / act
                ResultActions response = mockMvc.perform(get("/email/{email}", email));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNotFound());
        }

        @Test
        void testGivenPersonObject_whenSave_ThenReturnPerson() throws JsonProcessingException, Exception {
                // given / arrange
                given(personService.save(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

                // when / act
                ResultActions response = mockMvc.perform(post("/person")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(person)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                                .andExpect(jsonPath("$.address").value(person.getAddress()))
                                .andExpect(jsonPath("$.gender").value(person.getGender()))
                                .andExpect(jsonPath("$.email").value(person.getEmail()));

        }

        @Test
        void testGivenPersonObject_whenSave_ThenReturnEmailIsUsed() throws JsonProcessingException, Exception {
                // given / arrange
                given(personService.save(any(Person.class)))
                                .willThrow(new DatabaseException("Email already registered"));

                // when / act
                ResultActions response = mockMvc.perform(post("/person")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(person)));

                // then / assert
                response.andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Email already registered"));
        }

        @Test
        void testGivenUpdatePerson_whenUpdate_ThenReturnUpdatePerson() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.findById(id)).willReturn(person);
                willDoNothing().given(personService).update(person);

                // when / act
                Person updatePerson = new Person("John2", "Doe2", "Street2", "M", "jhon@email.com");
                updatePerson.setId(id);

                ResultActions response = mockMvc.perform(put("/person")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatePerson)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("Profile updated successfully"));
        }

        @Test
        void testGivenInvalidPersonId_whenUpdate_thenReturnNotFound() throws JsonProcessingException, Exception {
        // given
        Person updatePerson = new Person("John2", "Doe2", "Street2", "M", "jhon@email.com");
        updatePerson.setId(999L); // ID que não existe para forçar a exceção

        willThrow(new DatabaseException("Person not found, id: " + updatePerson.getId()))
            .given(personService).update(updatePerson);

        // when
        ResultActions response = mockMvc.perform(put("/person")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatePerson)));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("Person not found, id: " + updatePerson.getId()))
                .andExpect(jsonPath("$.status").value(404));
    }


        @Test
        void testGivenPersonId_whenDeleteById_ThenReturnNoContent() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.findById(id)).willReturn(person);
                willDoNothing().given(personService).deleteById(id);

                // when / act
                ResultActions response = mockMvc.perform(delete("/person/id/{id}", id));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGivenPersonId_whenDeleteById_ThenReturnNotFound() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                willThrow(DatabaseException.class).given(personService).deleteById(any(Long.class));

                // when / act

                ResultActions response = mockMvc.perform(delete("/id/{id}", id));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNotFound());
        }

}
