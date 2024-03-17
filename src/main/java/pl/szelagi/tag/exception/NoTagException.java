package pl.szelagi.tag.exception;

public class NoTagException extends SignTagException {
	public NoTagException(String elementName) {
		super("Not found sign with tag: " + elementName);
	}
}
