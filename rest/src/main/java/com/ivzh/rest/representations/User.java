package com.ivzh.rest.representations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Class for representation of contacts. It includes id, firstName,
 * lastName, phone, mail, comment.
 * Using JsonValidation
 */
public class User {

    private String lastName;
    private String firstName;

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format("firstName: %s , lastName: %s ", this.firstName, this.lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return lastName.equals(user.lastName) && firstName.equals(user.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName);
    }
}
