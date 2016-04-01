package com.awsmu.model;
import java.util.List;


import org.springframework.stereotype.Component;
@Component("stateModel")
public class StateModel {
	private String _id;
	private String country;
	private List<String> states;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public List<String> getStates() {
		return states;
	}
	public void setStates(List<String> states) {
		this.states = states;
	}

	
	
}
