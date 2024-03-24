package pl.szelagi.buildin.grouper.storage;

import pl.szelagi.buildin.grouper.Group;

import java.io.Serializable;

public class GroupState implements Serializable {
	private transient final Group group;

	public GroupState(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}
}
