package com.ucgen.common.exception.validation;

import com.ucgen.common.exception.BaseException;

public class ValidationException extends BaseException {

	private static final long serialVersionUID = 5440763306276287453L;

	public ValidationException() {
		super();
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
}
