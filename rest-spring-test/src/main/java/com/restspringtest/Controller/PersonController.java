package com.restspringtest.Controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restspringtest.Controller.Exception.StandartError;
import com.restspringtest.Controller.Exception.ValidationError;
import com.restspringtest.Model.Person;
import com.restspringtest.Model.Person.Create;
import com.restspringtest.Model.Person.DeleteById;
import com.restspringtest.Model.Person.FindByEmail;
import com.restspringtest.Model.Person.FindById;
import com.restspringtest.Model.Person.Update;
import com.restspringtest.Services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/person", produces = "application/json")
@Tag(name = "Person", description = "Person API")
public class PersonController {

        @Autowired
        private PersonService personService;

        @GetMapping(value = "/id/{id}")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully find by Id", content = @Content(schema = @Schema(implementation = Person.class))),
                        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandartError.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))) })
        @Operation(summary = "Find Person", description = "Find Person by Id", tags = { "GET" })
        public ResponseEntity<Person> findById(
                        @Validated(FindById.class) @PathVariable("id") @Parameter(description = "Unique identifier of the Person", example = "1") Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(personService.findById(id));
        }

        @GetMapping(value = "/email/{email}")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully find by Email", content = @Content(schema = @Schema(implementation = Person.class))),
                        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandartError.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))) })
        @Operation(summary = "Find Person", description = "Find Person by Email", tags = { "GET" })
        public ResponseEntity<Person> findByEmail(
                        @Validated(FindByEmail.class) @PathVariable("email") @Parameter(description = "Email of the Person", example = "joe@email.com") String email) {
                return ResponseEntity.status(HttpStatus.OK).body(personService.findByEmail(email));
        }

        @GetMapping
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully find all", content = @Content(schema = @Schema(implementation = Person.class))) })
        @Operation(summary = "Find All Persons", description = "Find All Persons", tags = { "GET" })
        public ResponseEntity<Page<Person>> findAll(
                        @PageableDefault(size = 10, page = 0, sort = {
                                        "id" }, direction = Direction.ASC) @ParameterObject Pageable pageable) {
                return ResponseEntity.status(HttpStatus.OK).body(personService.findAll(pageable));
        }

        @PutMapping
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully Update", content = @Content(schema = @Schema(implementation = Person.class))),
                        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandartError.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))) })
        @Operation(summary = "Update Person", description = "Update Person", tags = { "PUT" })
        public ResponseEntity<String> update(
                        @Validated(Update.class) 
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update Person", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class), examples = @ExampleObject(value = "{ \"id\": \"1\", \"firstName\": \"Joe\", \"lastName\": \"Doe\", \"address\": \"1234 Main St\", \"gender\": \"M\" }"))) 
                        @RequestBody Person person) {
                personService.update(person);
                return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
        }

        @PostMapping
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(schema = @Schema(implementation = Person.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid request, email is already used", content = @Content(schema = @Schema(implementation = StandartError.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))) })
        @Operation(summary = "Insert Person", description = "Insert Person", tags = { "POST" })
        public ResponseEntity<Person> save(
                        @Validated(Create.class) 
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Insert Person", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class), examples = @ExampleObject(value = "{\"firstName\": \"Joe\", \"lastName\": \"Doe\", \"address\": \"1234 Main St\", \"gender\": \"M\" }")))
                        @RequestBody Person person) {
                return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(person));
        }

        @DeleteMapping(value = "/id/{id}")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Successfully Delete", content = @Content(schema = @Schema(implementation = String.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
                        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandartError.class))) })
        @Operation(summary = "Delete Person", description = "Delete Person", tags = { "DELETE" })
        public ResponseEntity<String> deleteById(
                        @Validated(DeleteById.class) @PathVariable("id") @Parameter(description = "Unique identifier of the Person", example = "1") Long id) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(personService.deleteById(id));
        }

}