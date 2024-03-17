package pl.szelagi.state;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.state.manual.ManualContainerException;
import pl.szelagi.state.manual.PlayerManualContainer;

import java.io.Serializable;
import java.util.function.Predicate;

public class PlayerContainer<T extends PlayerState> implements Serializable {
	private final PlayerManualContainer<T> manualContainer;
	private final InstanceCreator<T> creator;

	public PlayerContainer(InstanceCreator<T> creator) {
		this.creator = creator;
		this.manualContainer = new PlayerManualContainer<>();
	}

	public @NotNull T get(Player player) throws ManualContainerException {
		if (manualContainer.isExists(player))
			return manualContainer.get(player);
		return manualContainer.create(player, creator);
	}

	public void clearPlayer(Player player) throws ManualContainerException {
		if (manualContainer.isExists(player))
			manualContainer.remove(player);
	}

	public @Nullable T find(Predicate<T> predicate) {
		return manualContainer.find(predicate);
	}
}
