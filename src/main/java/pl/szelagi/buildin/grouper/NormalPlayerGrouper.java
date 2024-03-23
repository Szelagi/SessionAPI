package pl.szelagi.buildin.grouper;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.event.player.initialize.InvokeType;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NormalPlayerGrouper extends BaseGrouper {
	private final ArrayList<BaseGroup> groups = new ArrayList<>();
	private final int exceptedGroups;
	private final GroupMaker maker;

	public NormalPlayerGrouper(ISessionComponent sessionComponent, int exceptedGroups, AllocatorType allocatorType, GroupMaker maker) {
		super(sessionComponent);
		this.exceptedGroups = exceptedGroups;
		this.maker = maker;
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		if (event.getInvokeType() == InvokeType.CHANGE)
			return;
		if (groups.size() < exceptedGroups) {
			var group = maker.make(groups.size());
			group.add(event.getPlayer());
			groups.add(group);
			return;
		}
		BaseGroup group = groups.stream().sorted()
		                        .findFirst()
		                        .orElseThrow();
		group.add(event.getPlayer());
	}

	@Override
	public List<Player> getAllPlayers() {
		return groups.stream()
		             .map(BaseGroup::getPlayers)
		             .flatMap(List::stream)
		             .collect(Collectors.toList());
	}

	@Override
	public List<Player> getAllInSessionPlayers() {
		return groups.stream()
		             .map(BaseGroup::getInSessionPlayers)
		             .flatMap(List::stream)
		             .collect(Collectors.toList());
	}

	@Override
	public List<BaseGroup> getGroups() {
		return groups;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public boolean isFair() {
		if (groups.isEmpty())
			return true;
		var firstSize = groups.get(0).count();
		return groups.stream()
		             .allMatch(baseGroup -> baseGroup.count() == firstSize);
	}

	@Override
	public boolean isEmpty() {
		return groups.stream()
		             .anyMatch(baseGroup -> baseGroup.count() > 0);
	}

	@Override
	public boolean hasPlayer(Player player) {
		return groups.stream()
		             .anyMatch(baseGroup -> baseGroup.hasPlayer(player));
	}

	@Override
	public @Nullable BaseGroup getUnfairGroup() {
		if (isFair() || groups.isEmpty())
			return null;
		return groups.stream().sorted()
		             .findFirst().orElseThrow();
	}

	@Override
	public @Nullable BaseGroup getGroup(Player player) {
		return groups.stream()
		             .filter(baseGroup -> baseGroup.hasPlayer(player))
		             .findFirst().orElse(null);
	}
}
