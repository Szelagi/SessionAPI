package pl.szelagi.buildin.controller.OtherEquipment;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.PlayerDestructorLambda;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.state.PlayerStateContainer;

public class OtherEquipment extends Controller {
    private final boolean isDefaultEquipment;

    public OtherEquipment(ISessionComponent sessionComponent, boolean isDefaultEquipment) {
        super(sessionComponent);
        this.isDefaultEquipment = isDefaultEquipment;
    }

    PlayerStateContainer<PlayerEqState> eqStatePlayerStateContainer = new PlayerStateContainer<>(PlayerEqState::new);

    @Override
    public void playerConstructor(Player player, InitializeType type) {
        super.playerConstructor(player, type);
        eqStatePlayerStateContainer.get(player).save();
        if (isDefaultEquipment) {
            player.getInventory().clear();
            player.clearActivePotionEffects();
            player.setHealthScale(20);
            player.setHealth(player.getHealthScale());
            player.setFoodLevel(20);
            player.setSaturation(0.6f);
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
        }
    }

    private PlayerDestructorLambda getPlayerDestructor(Player forPlayer) {
        var state = eqStatePlayerStateContainer.get(forPlayer);
        return (player, type) -> {
            state.load(player);
        };
    }

    @Override
    public void playerDestructor(Player player, UninitializedType type) {
        super.playerDestructor(player, type);
        getPlayerDestructor(player).run(player, type);
    }

    @Override
    public PlayerDestructorLambdas getPlayerDestructorRecovery(Player forPlayer) {
        var lambda = getPlayerDestructor(forPlayer);
        return super.getPlayerDestructorRecovery(forPlayer).add(lambda);
    }

    @NotNull
    @Override
    public String getName() {
        return "otherEquipment";
    }
}