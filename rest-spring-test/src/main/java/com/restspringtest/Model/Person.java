package com.restspringtest.Model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_person")
@Schema(description = "Represents a person in the system")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface Update {}
    public interface DeleteById {}
    public interface Create {}
    public interface FindById {}
    public interface FindByEmail {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier of the person", example = "1", accessMode = AccessMode.READ_ONLY)
    @NotNull(message = "The field id is required", groups = {DeleteById.class, FindById.class})
    private Long id;

    @Column(name = "first_name", length = 20)
    @Schema(description = "First Name of a person", example = "John", required = true)
    @NotBlank(message = "The field firstname is required", groups = {Create.class, Update.class})
    private String firstName;

    @Column(name = "last_name", length = 20)
    @Schema(description = "Last Name of a person", example = "Doe", required = true)
    @NotBlank(message = "The field lastname is required", groups = {Create.class, Update.class})
    private String lastName;

    @Column(name = "address", length = 100)
    @Schema(description = "Adress of a person", example = "Street", required = true)
    @NotBlank(message = "The field address is required", groups = {Create.class, Update.class})
    private String address;

    @Column(name = "gender", length = 10)
    @Schema(description = "Gender of a person", example = "M", required = true)
    @NotBlank(message = "The field gender is required", groups = {Create.class, Update.class})
    private String gender;

    @Column(name = "email", length = 50)
    @Schema(description = "Email address of a person", example = "johndoe@example.com", required = true)
    @NotBlank(message = "The field email is required", groups = {Create.class, Update.class, FindByEmail.class})
    @Email(message = "The field email must be a valid email")
    private String email;
    
    public Person() {
    }

    public Person(String firstName, String lastName, String address, String gender, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.email = email;
    }
}
