package pl.szelagi.component.controller.exception;

import pl.szelagi.component.baseexception.StartException;

public class ControllerStartException extends StartException {
	public ControllerStartException(String name) {
		super(name);
	}
}
