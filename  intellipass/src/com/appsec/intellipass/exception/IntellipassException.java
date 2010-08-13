package com.appsec.intellipass.exception;

/**
 * 
 * @author Chander Singh [chander (dot) singh (at) gmail (dot) com]
 * @ Created on Apr 16, 2009
 */
public class IntellipassException extends Exception {
	private static final long serialVersionUID = 3043622795138210455L;
	
	private String errorCode;
	
	/**
	 * 
	 * @param message
	 */
	public IntellipassException (String message) {
		super (message);
	}
	
	/**
	 * 
	 * @param errorCode
	 * @param message
	 */
	public IntellipassException (String errorCode, String message) {
		super (message);
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}	
}
