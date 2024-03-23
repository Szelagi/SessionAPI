package pl.szelagi.buildin.grouper;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;

import java.util.List;

public abstract class BaseGrouper<T extends BaseGroup> extends Controller {
	public BaseGrouper(ISessionComponent component) {
		super(component);
	}

	public abstract boolean hasPlayer(Player player);

	public abstract @Nullable T getUnfairGroup();

	public abstract @Nullable T getGroup(Player player);

	public abstract List<Player> getAllPlayers();

	public abstract List<Player> getAllInSessionPlayers();

	public abstract List<T> getGroups();

	public abstract int getGroupCount();

	public abstract boolean isFair();

	public abstract boolean isEmpty();
}
