package pl.szelagi.buildin.controller.OtherEquipment;

import org.bukkit.entity.Player;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.constructor.PlayerDestructorLambda;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.event.player.recovery.PlayerRecoveryEvent;
import pl.szelagi.state.PlayerContainer;

public class OtherEquipment extends Controller {
	private final boolean isClearEquipment;
	PlayerContainer<PlayerEqState> eqStatePlayerContainer = new PlayerContainer<>(PlayerEqState::new);

	public OtherEquipment(ISessionComponent sessionComponent) {
		super(sessionComponent);
		this.isClearEquipment = true;
	}

	public OtherEquipment(ISessionComponent sessionComponent, boolean cloneEquipment) {
		super(sessionComponent);
		this.isClearEquipment = !cloneEquipment;
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		var player = event.getPlayer();
		eqStatePlayerContainer.get(player).save();
		if (isClearEquipment) {
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
		var state = eqStatePlayerContainer.get(forPlayer);
		return state::load;
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
		var player = event.getPlayer();
		getPlayerDestructor(player).run(player);
	}

	@Override
	public void playerDestructorRecovery(PlayerRecoveryEvent event) {
		super.playerDestructorRecovery(event);
		var lambda = getPlayerDestructor(event.getForPlayer());
		event.getLambdas().add(lambda);
	}
}
