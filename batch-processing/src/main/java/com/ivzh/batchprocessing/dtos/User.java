package com.ivzh.batchprocessing.dtos;

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

}
