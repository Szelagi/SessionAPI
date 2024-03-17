package pl.szelagi.buildin.controller.OtherEquipment;

import org.bukkit.entity.Player;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.PlayerDestructorLambda;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.controller.Controller;
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
	public void playerConstructor(Player player, InitializeType type) {
		super.playerConstructor(player, type);
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
}
