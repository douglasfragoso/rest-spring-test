package com.restspringtest.Integration.Tests;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restspringtest.Configurations.TestConfiguration;
import com.restspringtest.Integration.TestContainers.AbstractIntegrationTest;
import com.restspringtest.Model.Person;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfiguration.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("John", "Doe", "Street", "M", "john@email.com");
    }

    @Test
    @Order(1)
    public void integrationTestGivenPerson_whenPostPerson_thenReturnPerson() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfiguration.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
        assertEquals(person.getLastName(), createdPerson.getLastName());
    }

    @Test
    @Order(2)
    public void integrationTestGivenPerson_whenPutPerson_thenReturnString() throws JsonProcessingException {

        Person updatePerson = new Person("John2", "Doe2", "Street2", "M", "john@email.com");
        updatePerson.setId(person.getId());

        var responseMessage = given().spec(specification)
                .contentType(TestConfiguration.CONTENT_TYPE_JSON)
                .body(updatePerson)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertEquals("Profile updated successfully", responseMessage);
        assertNotEquals(person.getFirstName(), updatePerson.getFirstName());
        assertNotEquals(person.getLastName(), updatePerson.getLastName());
    }

    @Test
    @Order(3)
    public void integrationTestGivenId_whenGetPersonById_thenReturnPerson() throws JsonProcessingException {

        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertEquals("John2", createdPerson.getFirstName());
        assertEquals("Doe2", createdPerson.getLastName());
    }

    @Test
    @Order(4)
    public void integrationTestGivenEmail_whenGetPersonByEmail_thenReturnPerson() throws JsonProcessingException {

        var content = given().spec(specification)
                .pathParam("email", person.getEmail())
                .when()
                .get("/email/{email}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertEquals("John2", createdPerson.getFirstName());
        assertEquals("Doe2", createdPerson.getLastName());
    }

    @Test
    @Order(5)
    public void integrationTest_whenGetAllPersons_thenReturnListOfPerson() throws JsonProcessingException {

        Person person2 = new Person("Johna", "Doea", "Streeta", "F", "johna@email.com");

        given().spec(specification)
                .contentType(TestConfiguration.CONTENT_TYPE_JSON)
                .body(person2)
                .when()
                .post()
                .then()
                .statusCode(201);

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Map<String, Object> responseMap = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {});

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> personList = (List<Map<String, Object>>) responseMap.get("content");
        int totalElements = (int) responseMap.get("totalElements");
        int totalPages = (int) responseMap.get("totalPages");

        List<Person> persons = new ArrayList<>();
        for (Map<String, Object> personMap : personList) {
            Person person = objectMapper.convertValue(personMap, Person.class);
            persons.add(person);
        }

        Pageable pageable = PageRequest.of(0, 10); 
        Page<Person> page = new PageImpl<>(persons, pageable, totalElements);

        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size()); 
        assertEquals(totalElements, page.getTotalElements());
        assertEquals(totalPages, page.getTotalPages()); 
    }

    @Test
    @Order(6)
    public void integrationTestGivenId_whenGetDeletePersonById_thenNoContent() throws JsonProcessingException {
            
        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("/id/{id}")
                .then()
                .statusCode(204);
    }


}
