package pl.szelagi.component.session.cause;
import pl.szelagi.util.ServerWarning;

public class ExceptionCause extends StopCause {
    public ExceptionCause(String reason) {
        super(reason);
        new ServerWarning(reason);
    }
}
