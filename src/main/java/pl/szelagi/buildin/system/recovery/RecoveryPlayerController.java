package pl.szelagi.buildin.system.recovery;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;
import pl.szelagi.recovery.RecoveryFileManager;
import pl.szelagi.util.timespigot.Time;

public class RecoveryPlayerController extends Controller {
    private final RecoveryFileManager recoveryFileManager = new RecoveryFileManager();
    private boolean isDestructed = false;
    public RecoveryPlayerController(Session sessionComponent) {
        super(sessionComponent);
    }

    @Nullable
    @Override
    public Listener getListener() {
        return super.getListener();
    }

    @Override
    public void constructor() {
        super.constructor();
        getProcess().runControlledTaskTimer(this::save, Time.Seconds(1), Time.Seconds(1));
    }

    @Override
    public void playerConstructor(Player player, InitializeType type) {
        super.playerConstructor(player, type);
        recoveryFileManager.savePlayerRecovery(player);
    }

    @Override
    public void playerDestructor(Player player, UninitializedType type) {
        super.playerDestructor(player, type);
        if (recoveryFileManager.existsPlayerRecovery(player)) {
            recoveryFileManager.deletePlayerRecovery(player);
        }
    }

    @Override
    public void destructor() {
        super.destructor();
        isDestructed = true;
    }

    public void save() {
        if (isDestructed) return;
        for (var player : getSession().getPlayers()) {
            recoveryFileManager.savePlayerRecovery(player);
        }
    }

    @Override
    public @NotNull String getName() {
        return "SystemRecoveryPlayerController";
    }
}
