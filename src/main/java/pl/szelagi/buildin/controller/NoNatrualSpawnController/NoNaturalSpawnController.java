package pl.szelagi.buildin.controller.NoNatrualSpawnController;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;

public class NoNaturalSpawnController extends Controller {
    public NoNaturalSpawnController(ISessionComponent component) {
        super(component);
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new NoNaturalSpawnListener();
    }

}