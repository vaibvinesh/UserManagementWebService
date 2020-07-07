package com.springcourse.project.exeptions;

public class UserServiceException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1751425258864887402L;

	public UserServiceException(String message) {
		super(message);
	}
}
