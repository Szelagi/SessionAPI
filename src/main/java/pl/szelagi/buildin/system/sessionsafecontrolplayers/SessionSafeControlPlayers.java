package pl.szelagi.buildin.system.sessionsafecontrolplayers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.state.PlayerContainer;

import java.util.ArrayList;
import java.util.List;

public class SessionSafeControlPlayers extends Controller {
    private static final ArrayList<PotionEffectType> NEGATIVE_POTION_TYPES = new ArrayList<>(List.of(
            PotionEffectType.POISON,
            PotionEffectType.WITHER,
            PotionEffectType.WEAKNESS,
            PotionEffectType.DARKNESS,
            PotionEffectType.BLINDNESS,
            PotionEffectType.SLOW,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.LEVITATION,
            PotionEffectType.CONFUSION,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.HUNGER,
            PotionEffectType.GLOWING
    ));

    private final PlayerContainer<SessionSafeControlPlayerState> stateContainer = new PlayerContainer<>(SessionSafeControlPlayerState::new);

    public SessionSafeControlPlayers(ISessionComponent sessionComponent) {
        super(sessionComponent);
    }

    @Override
    public void playerConstructor(Player player, InitializeType type) {
        super.playerConstructor(player, type);
        stateContainer.get(player).save();
    }
    @Override
    public void playerDestructor(Player player, UninitializedType type) {
        var state = stateContainer.get(player);
        super.playerDestructor(player, type);
        player.setFallDistance(0);
        player.setFireTicks(0);
        for (var potionType : NEGATIVE_POTION_TYPES) player.removePotionEffect(potionType);
        state.load(player);
    }

    @Override
    public PlayerDestructorLambdas getPlayerDestructorRecovery(Player forPlayer) {
        var state = stateContainer.get(forPlayer);
        return super.getPlayerDestructorRecovery(forPlayer).add((player, type) -> {
            player.setFallDistance(0);
            player.setFireTicks(0);
            for (var potionType : NEGATIVE_POTION_TYPES) player.removePotionEffect(potionType);
            state.load(player);
        });
    }

    @Override
    public @NotNull String getName() {
        return "SystemSafeControlController";
    }
}
