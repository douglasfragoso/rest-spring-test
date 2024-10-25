package com.restspringtest.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     public ResponseEntity<Person> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findById(id));
    }

    @GetMapping(value = "/email/{email}")
     public ResponseEntity<Person> findByEmail(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<Page<Person>> findAll(
        @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.findAll(pageable));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Person person) {
        personService.update(person);
        return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
    }

    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(person));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        personService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(personService.deleteById(id));
    }

}