package com.awsmu.model;
import java.util.List;


import org.springframework.stereotype.Component;

@Component("cityModel")
public class CityModel {
	private String _id;
	private String country;
	private String state;
	private List<String> cities;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<String> getCities() {
		return cities;
	}
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	
	
	
}
