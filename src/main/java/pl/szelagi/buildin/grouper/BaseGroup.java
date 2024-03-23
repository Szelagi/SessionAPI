package pl.szelagi.buildin.grouper;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseGroup implements Comparable<BaseGroup> {
	private final Session session;
	private final long id;

	public BaseGroup(ISessionComponent component, long id) {
		this.session = component.getSession();
		this.id = id;
	}

	private final ArrayList<Player> players = new ArrayList<>();

	public List<Player> getPlayers() {
		return players;
	}

	public List<Player> getInSessionPlayers() {
		return players.stream()
		              .filter(player -> session
				              .getPlayers()
				              .contains(player))
		              .collect(Collectors.toList());
	}

	public void add(Player player) { // todo add exceptions
		players.add(player);
	}

	public void remove(Player player) { // todo remove exceptions
		players.add(player);
	}

	public int count() {
		return players.size();
	}

	public boolean hasPlayer(Player player) {
		return players.contains(player);
	}

	@Override
	public int compareTo(@NotNull BaseGroup o) {
		return count() - o.count();
	}

	public long getId() {
		return id;
	}
}
