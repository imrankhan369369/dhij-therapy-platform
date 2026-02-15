package com.dhij.app.com.dhij.app.exception;

public class HelperNotFoundException extends RuntimeException{

	public HelperNotFoundException(String resourceName, String fieldName,Long fieldValue) {
		super(String.format("%s not found with %s:%s", resourceName,fieldName,fieldValue));
	}
}
