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
import com.restspringtest.Services.Exception.ExceptionBusinessRules;

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
                given(personService.findById(id)).willReturn(person); // Retorna diretamente a entidade 'person'

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
                given(personService.findById(id)).willThrow(ExceptionBusinessRules.class);

                // when / act
                ResultActions response = mockMvc.perform(get("/person/id/{id}", id));

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
                given(personService.findByEmail(email)).willThrow(ExceptionBusinessRules.class);

                // when / act
                ResultActions response = mockMvc.perform(get("/person/email/{email}", email));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNotFound());
        }

        @Test
        void testGivenUpdatePerson_whenUpdate_ThenReturnUpdatePerson() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.findById(id)).willReturn(person);
                given(personService.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

                // when / act
                Person updatePerson = new Person("John2", "Doe2", "Street2", "M", "jhon@email.com");

                ResultActions response = mockMvc.perform(put("/person")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatePerson)));

                // then / assert
                // then / assert
                response.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("Profile updated successfully"));
        }

        @Test
        void testGivenUpdatePerson_whenUpdate_ThenReturnNotFound() throws JsonProcessingException, Exception {
                // given / arrange
                given(personService.update(any(Person.class)))
                                .willThrow(ExceptionBusinessRules.class);

                // when / act
                Person updatePerson = new Person("John2", "Doe2", "Street2", "M", "jhon@email.com");

                ResultActions response = mockMvc.perform(put("/person")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatePerson)));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNotFound());
        }

        @Test
        void testGivenPersonId_whenDeleteById_ThenReturnString() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.findById(id)).willReturn(person);
                given(personService.deleteById(id)).willReturn("Profile is deleted successfully");

                // when / act
                ResultActions response = mockMvc.perform(delete("/person/id/{id}", id));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNoContent())
                                .andExpect(content().string("Profile is deleted successfully"));
        }

        @Test
        void testGivenPersonId_whenDeleteById_ThenReturnNotFound() throws JsonProcessingException, Exception {
                // given / arrange
                Long id = 1L;
                given(personService.deleteById(any(Long.class)))
                                .willThrow(ExceptionBusinessRules.class);

                // when / act

                ResultActions response = mockMvc.perform(delete("/person/id/{id}", id));

                // then / assert
                response.andDo(print())
                                .andExpect(status().isNotFound());
        }

}
