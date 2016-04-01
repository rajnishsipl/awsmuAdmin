package com.awsmu.entity;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "attributes")
public class Specialties {
	private String _id;
	private String attr;
	private List<String> value;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}
	
	
}