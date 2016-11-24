package com.ucgen.common.exception.definition;

public class MissingDefinitionException extends DefinitionException {

	private static final long serialVersionUID = -9073224022774445224L;

	public MissingDefinitionException() {
		super();
	}
	
	public MissingDefinitionException(String message) {
		super(message);
	}
	
}
