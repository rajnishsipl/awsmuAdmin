/**
 * 
 */
package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.awsmu.entity.NationalityValues;

/**
 * Handles nationality list.
 *
 */

@Component("nationalityModel")
public class NationalityModel {
	
	private String _id;
	private String attr;
	private List<NationalityValues> value;
	
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
	
	public List<NationalityValues> getValue() {
		return value;
	}

	public void setValue(List<NationalityValues> value) {
		this.value = value;
	}
	
	public void getCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date setCreatedDate() {
		return createdDate;
	} 
		
}
