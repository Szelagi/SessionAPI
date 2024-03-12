package pl.szelagi.buildin.controller.NoCreatureDropController;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;

public class NoCreatureDropController extends Controller {
    public NoCreatureDropController(ISessionComponent component) {
        super(component);
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new NoCreatureDropListener();
    }

    @NotNull
    @Override
    public String getName() {
        return "noCreatureDrop";
    }
}
