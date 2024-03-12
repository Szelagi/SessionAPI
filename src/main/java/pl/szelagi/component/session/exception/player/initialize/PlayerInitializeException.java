package pl.szelagi.component.session.exception.player.initialize;


import pl.szelagi.util.ServerRuntimeException;

public class PlayerInitializeException extends ServerRuntimeException {
    public PlayerInitializeException(String name) {
        super(name);
    }
}
