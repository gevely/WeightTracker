package com.gesoftware.weights.data;

public class Person {

	private long id;
	private String firstName;
	
	
	public Person() {
		super();
		this.firstName = "New";
	}

	public Person(long id, String firstName) {
		super();
		this.id = id;
		this.firstName = firstName;
	}

	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return firstName;
	}
}
