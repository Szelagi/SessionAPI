package pl.szelagi.bukkitadapted;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;

public class ItemStackAdapted implements Serializable {
	private transient ItemStack itemStack;

	public ItemStackAdapted(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Serial
	private void writeObject(ObjectOutputStream oos) throws IOException {
		var bos = new BukkitObjectOutputStream(oos);
		bos.writeObject(itemStack);
	}

	@Serial
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		var bis = new BukkitObjectInputStream(ois);
		this.itemStack = (ItemStack) bis.readObject();
	}

	public ItemStack getItemStack() {
		return itemStack;
	}
}