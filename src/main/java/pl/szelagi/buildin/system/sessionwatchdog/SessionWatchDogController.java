package pl.szelagi.buildin.system.sessionwatchdog;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;

public class SessionWatchDogController extends Controller {
    public SessionWatchDogController(ISessionComponent sessionComponent) {
        super(sessionComponent);
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new SessionWatchDogListener();
    }

    @NotNull
    @Override
    public String getName() {
        return "sessionWatchDogController";
    }
}
