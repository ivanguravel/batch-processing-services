package com.ivzh.batchprocessing.dtos;

import java.util.Objects;
import java.util.Random;

public class User {

	private final int id;
	private final String lastName;
	private final String firstName;

	public User(String firstName, String lastName) {
		this.id = new Random().nextInt();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getId() {
		return id;
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
