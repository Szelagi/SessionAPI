package pl.szelagi.component.baseexception.multi;

import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.baseexception.StartException;

public class MultiStartException extends StartException {
	public MultiStartException(ISessionComponent component) {
		super(component.getName() + ", " + BaseComponent.toType(component).name());
	}
}
