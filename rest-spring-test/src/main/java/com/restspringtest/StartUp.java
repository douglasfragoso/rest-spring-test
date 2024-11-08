package com.restspringtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Spring Boot API With Tests", version = "1.0", description = "API do curso de Java Unit Testing com Spring Boot 3, TDD, Junit 5 e Mockito do Prof. Leandro Costa",
contact = @Contact(name = "Douglas Fragoso", email = "douglas.iff@gmail.com", url = "https://github.com/douglasfragoso"), 
license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")))
public class StartUp {

	public static void main(String[] args) {
		SpringApplication.run(StartUp.class, args);
	}

}
