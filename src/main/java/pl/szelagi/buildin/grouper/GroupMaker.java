package pl.szelagi.buildin.grouper;

public interface GroupMaker<T extends BaseGroup> {
	T make(long id);
}
