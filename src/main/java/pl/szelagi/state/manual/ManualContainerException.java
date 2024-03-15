package pl.szelagi.state.manual;

import pl.szelagi.util.ServerRuntimeException;

public class ManualContainerException extends ServerRuntimeException {
    public ManualContainerException(String name) {
        super(name);
    }
}
