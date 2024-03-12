package pl.szelagi.component;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.session.Session;
import pl.szelagi.process.IControlProcess;

public interface ISessionComponent {
    @NotNull IControlProcess getProcess();
    @NotNull Session getSession();
    @NotNull JavaPlugin getPlugin();
    @NotNull String getName();
}