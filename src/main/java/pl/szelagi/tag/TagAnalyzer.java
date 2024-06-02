package pl.szelagi.tag;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativeLocation;
import pl.szelagi.spatial.ISpatial;
import pl.szelagi.tag.exception.SignTagException;

import java.util.ArrayList;
import java.util.Arrays;

public class TagAnalyzer {
	private final static ArrayList<Material> SIGN_MATERIALS = new ArrayList<>(Arrays.asList(Material.OAK_SIGN, Material.OAK_WALL_SIGN));
	private final static String PREFIX_DEFAULT = "@";

	public static TagResolve process(@NotNull final ISpatial ISpatial) {
		return process(ISpatial, PREFIX_DEFAULT, false);
	}

	public static TagResolve process(@NotNull ISpatial spatial, @NotNull String prefix, boolean deleteSign) {
		var signTagData = new TagResolve();
		spatial.eachBlocks(block -> {
			var material = block.getType();
			if (!SIGN_MATERIALS.contains(material))
				return;
			var sign = (Sign) block.getState();
			var signSide = sign.getSide(Side.FRONT);
			var mainLine = signSide.getLine(0);
			if (!mainLine.startsWith(prefix))
				return;
			var tagName = mainLine.replace(prefix, "");
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
