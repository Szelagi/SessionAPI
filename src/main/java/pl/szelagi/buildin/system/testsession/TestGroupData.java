package pl.szelagi.buildin.system.testsession;

import org.bukkit.Location;
import pl.szelagi.buildin.grouper.Group;
import pl.szelagi.buildin.grouper.storage.GroupState;

public class TestGroupData extends GroupState {
	private final Location spawn;
	private final String role;

	public TestGroupData(Group group, Location spawn, String role) {
		super(group);
		this.spawn = spawn;
		this.role = role;
	}

	public Location getSpawn() {
		return spawn;
	}

	public String getRole() {
		return role;
	}
}
