package pl.szelagi.space;

import pl.szelagi.util.ServerRuntimeException;

public class DeallocateException extends ServerRuntimeException {
    public DeallocateException(String name, Space space) {
        super(name + "in " + space.toString());
    }
}