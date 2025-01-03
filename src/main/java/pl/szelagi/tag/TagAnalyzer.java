package pl.szelagi.tag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.relative.RelativeLocation;
import pl.szelagi.spatial.ISpatial;
import pl.szelagi.spatial.SpatialResolve;
import pl.szelagi.tag.exception.SignTagException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

public class TagAnalyzer {
	private static final int PART_SIZE = 70;
	private final static ArrayList<Material> SIGN_MATERIALS = new ArrayList<>(Arrays.asList(Material.OAK_SIGN, Material.OAK_WALL_SIGN));
	private final static String PREFIX = "@";

	public static void async(@NotNull ISpatial spatial, @NotNull Consumer<TagResolve> callback) {
		var parts = spatial.partition(PART_SIZE);
		var iterator = parts.iterator();
		var globalResolve = new TagResolve();
		var instance = SessionAPI.getInstance();

		var nextElement = new Runnable() {
			@Override
			public void run() {
				var resolved = process(iterator.next());
				globalResolve.add(resolved);
				if (iterator.hasNext()) {
					Bukkit.getScheduler().runTaskLater(instance, this, 1L);
				} else {
					callback.accept(globalResolve);
				}
			}
		};

		Bukkit.getScheduler().runTask(instance, nextElement);
	}

	private static TagResolve process(@NotNull ISpatial spatial) {
		var signTagData = new TagResolve();
		spatial.eachBlocks(block -> {
			var material = block.getType();
			if (!SIGN_MATERIALS.contains(material))
				return;
			var sign = (Sign) block.getState();
			var signSide = sign.getSide(Side.FRONT);
			var mainLine = signSide.getLine(0);
			if (!mainLine.startsWith(PREFIX))
				return;
			var tagName = mainLine.replace(PREFIX, "");
			var args = Arrays
					.stream(signSide.getLines())
					.skip(1).toList();
			var relativeLocation = new RelativeLocation(block.getLocation(), spatial.getCenter());
			var blockData = block.getBlockData();
			BlockFace blockFace;

			if (material == Material.OAK_SIGN && blockData instanceof Rotatable rotatable) {
				// SIGN
				blockFace = rotatable.getRotation();
			} else if (material == Material.OAK_WALL_SIGN && blockData instanceof Directional directional) {
				// WALL SIGN
				blockFace = directional.getFacing();
			} else {
				throw new SignTagException("Sign is not Rotatable or Directional");
			}
			signTagData.add(new Tag(tagName, relativeLocation, blockFace, args));
		});
		return signTagData;
	}
}
