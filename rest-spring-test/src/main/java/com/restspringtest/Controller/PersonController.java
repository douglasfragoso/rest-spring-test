package com.restspringtest.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restspringtest.Model.Person;
import com.restspringtest.Services.PersonService;

@RestController
@RequestMapping(value = "/person", produces = "application/json")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/id/{id}")
     public ResponseEntity<Optional<Person>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Person>> findAll(
        @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findAll(pageable));
    }

}