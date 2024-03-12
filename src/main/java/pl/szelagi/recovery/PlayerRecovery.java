package pl.szelagi.recovery;

import org.bukkit.entity.Player;
import pl.szelagi.component.constructor.PlayerDestructorLambda;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.process.MainProcess;
import pl.szelagi.recovery.exception.RecoveryException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public final class PlayerRecovery implements Serializable {
    private static ArrayList<PlayerDestructorLambda> getControllerPlayerDestructor(Player player, MainProcess mainProcess) {
        var destructors = new ArrayList<PlayerDestructorLambda>();
        for (var c : mainProcess.getControllers()) {
            var localDestructors = c.getPlayerDestructorRecovery(player);
            destructors.addAll(localDestructors.getLambdas());
        }
        Collections.reverse(destructors);
        return destructors;
    }


    private final ArrayList<PlayerDestructorLambda> destructors;

    // Warning! Player UUID maybe can change!
    private final String playerAccountName;


    public PlayerRecovery(Player player) throws RecoveryException {
        var dungeon = SessionManager.getSession(player);
        if (dungeon == null) throw new RecoveryException("player is not in session");
        var playerDestructors = getControllerPlayerDestructor(player, dungeon.getMainProcess());

        playerDestructors.addAll(dungeon.getCurrentBoard().getPlayerDestructorRecovery(player).getLambdas());
        playerDestructors.addAll(dungeon.getPlayerDestructorRecovery(player).getLambdas());

        this.destructors = playerDestructors;
        this.playerAccountName = player.getName();
    }

    public ArrayList<PlayerDestructorLambda> getDestructors() {
        return destructors;
    }

    public void run(Player player) throws RecoveryException {
        if (!playerAccountName.equals(player.getName())) throw new RecoveryException("player name is other than saved!");
        for (var destructor : destructors) {
            destructor.run(player, UninitializedType.RECOVERY_DESTRUCTOR);
        }
    }
}