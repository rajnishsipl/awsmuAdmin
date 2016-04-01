/**
 * 
 */
package com.awsmu.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles profession list.
 *
 */
@Document(collection = "attributes")
public class Profession {
	
	private String _id;
	private String attr;
	private List<ProfessionValues> value; ;
	
	private Date createdDate;
	

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
	
	public List<ProfessionValues> getValue() {
		return value;
	}

	public void setValue(List<ProfessionValues> value) {
		this.value = value;
	}
	
	public void getCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date setCreatedDate() {
		return createdDate;
	} 
		
}
