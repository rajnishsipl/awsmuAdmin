package com.awsmu.exception;

// Class for handling exception 

public class AwsmuException extends Exception {
	private static final long serialVersionUID = 1L;
	// code is the exception status code
	private int code;
	// errorMsg is the actual exception message 
	private String errorMsg;
	// displayMsg is the custom error message
	private String displayMsg;
	
	private String stackTrace;
	// parameterized constructor 
	public AwsmuException(int code, String errorMsg, String displayMsg) {
		this.code = code;
		this.errorMsg = errorMsg;
		this.displayMsg = displayMsg;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String getDisplayMsg() {
		return displayMsg;
	}


	public void setDisplayMsg(String displayMsg) {
		this.displayMsg = displayMsg;
	}


	@Override
	public String toString() {
		
		StackTraceElement[] stack = this.getStackTrace();		
		for(StackTraceElement line : stack)
		{
			stackTrace += line.toString();
		}
		return "AwsmuException [code=" + code + ", errorMsg=" + errorMsg
				+ ", displayMsg=" + displayMsg + ", stackTrace=" + stackTrace
				+ "]";
	}


	
	
	
	
}
