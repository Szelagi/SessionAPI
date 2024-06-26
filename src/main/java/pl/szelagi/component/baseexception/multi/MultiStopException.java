package pl.szelagi.component.baseexception.multi;

import pl.szelagi.component.ComponentType;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.baseexception.StartException;

public class MultiStopException extends StartException {
	public MultiStopException(ISessionComponent component) {
		super(component.getName() + ", " + ComponentType.toType(component).name());
	}
}
