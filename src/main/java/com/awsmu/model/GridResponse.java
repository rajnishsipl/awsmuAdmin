package com.awsmu.model;

import java.util.List;

/**
 * Class handles the response of grid
 */
public class GridResponse {
	private boolean isLoggedIn = true;
	private boolean status=true;
	private int code = 200;
	private String message;
	private int page;
	private int records;
	private int total;
	private List<Object> rows;

	public boolean isIsLoggedIn() {
		return this.isLoggedIn;
	}

	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	


	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Object> getRows() {
		return rows;
	}
	
	public void setRows(List<Object> rows) {
		this.rows = rows;
	}
	
	public void setRow(Object row) {
		this.rows.add(row);
	}
	
	@Override
	public String toString() {
		return "GridResponse [page=" + page + ", records=" + records
				+ ", total=" + total + ", rows=" + rows + "]";
	}
	
}
