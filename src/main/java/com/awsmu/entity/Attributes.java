package com.awsmu.entity;



import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attributes")
public class Attributes {
	private String _id;
	private String attr;

	 private Date created_date;
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	
}
