package com.gesoftware.weights.data;

public class Weight {
	
	private long id;
	private long personId;
	private double weight;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getPersonId() {
		return personId;
	}
	
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return Double.toString(weight);
	}
}
