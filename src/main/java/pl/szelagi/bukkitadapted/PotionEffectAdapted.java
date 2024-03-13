package pl.szelagi.bukkitadapted;

import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;

public class PotionEffectAdapted implements Serializable {
    private transient PotionEffect potionEffect;

    public PotionEffectAdapted(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
    }

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        var bos = new BukkitObjectOutputStream(oos);
        bos.writeObject(potionEffect);
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        var bis = new BukkitObjectInputStream(ois);
        this.potionEffect = (PotionEffect) bis.readObject();
    }
}
