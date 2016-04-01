/**
 * 
 */
package com.awsmu.model;

import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Component;

/**
 * Handles profession list.
 *
 */
@Component("professionModel")
public class ProfessionModel {
	
	private String _id;
	private String attr;
	private List<ProfessionValuesModel> value; ;
	
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
	
	public List<ProfessionValuesModel> getValue() {
		return value;
	}

	public void setValue(List<ProfessionValuesModel> value) {
		this.value = value;
	}
	
	public void getCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date setCreatedDate() {
		return createdDate;
	} 
		
}
