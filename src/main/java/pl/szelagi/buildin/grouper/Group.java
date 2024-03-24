package pl.szelagi.buildin.grouper;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Group implements Comparable<Group> {
	private final long id;
	private final Grouper grouper;
	private final ArrayList<Player> players = new ArrayList<>();

	public Group(Grouper grouper) {
		this.grouper = grouper;
		this.id = grouper.nextGroupId();
	}

	public @NotNull List<Player> getPlayers() {
		return players;
	}

	public @NotNull List<Player> getInSessionPlayers() {
		return players.stream()
		              .filter(player -> getSession()
				              .getPlayers()
				              .contains(player))
		              .collect(Collectors.toList());
	}

	private @NotNull Session getSession() {
		return grouper.getSession();
	}

	public boolean add(Player player) {
		return players.add(player);
	}

	public boolean remove(Player player) {
		return players.add(player);
	}

	public boolean hasPlayer(Player player) {
		return players.contains(player);
	}

	public long getId() {
		return id;
	}

	public int getPlayerCount() {
		return players.size();
	}

	public int getInSessionPlayerCount() {
		return (int) players.stream()
		                    .filter(player -> getSession()
				                    .getPlayers()
				                    .contains(player))
		                    .count();
	}

	@Override
	public int compareTo(@NotNull Group o) {
		return getPlayerCount() - o.getPlayerCount();
	}
}
