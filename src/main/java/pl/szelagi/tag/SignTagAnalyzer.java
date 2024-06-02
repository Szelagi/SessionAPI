package pl.szelagi.tag;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativeLocation;
import pl.szelagi.spatial.ISpatial;

import java.util.ArrayList;
import java.util.Arrays;

public class SignTagAnalyzer {
	private final static ArrayList<Material> SIGN_MATERIALS = new ArrayList<>(Arrays.asList(Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.OAK_HANGING_SIGN, Material.OAK_WALL_HANGING_SIGN));
	private final static String PREFIX_DEFAULT = "@";

	private static String[] getArgs(String[] lines) {
		String[] args = new String[lines.length - 1];
		System.arraycopy(lines, 1, args, 0, args.length);
		return args;
	}

	public static SignTagData process(@NotNull ISpatial spatial, @NotNull String prefix, boolean deleteSign) {
		SignTagData data = new SignTagData();
		Sign sign;
		SignSide signSide;
		String mainLine;
		String name;
		String[] args;
		SignTag element;
		var blocks = spatial.getBlocksIn();
		for (var b : blocks) {
			if (!SIGN_MATERIALS.contains(b.getType()))
				continue;

			sign = (Sign) b.getState();
			signSide = sign.getSide(Side.FRONT);
			mainLine = signSide.getLine(0);
			if (mainLine.startsWith(prefix)) {
				name = mainLine.replace(prefix, "");
				args = getArgs(signSide.getLines());
				var relativeLocation = new RelativeLocation(b.getLocation(), spatial.getCenter());

				BlockFace blockFace = BlockFace.EAST;
				if (b.getType() == Material.OAK_WALL_SIGN) {
					BlockData blockData = b.getBlockData();
					if (blockData instanceof Directional) {
						Directional directional = (Directional) blockData;
						blockFace = directional.getFacing();
					}
				} else if (b.getType() == Material.OAK_SIGN) {
					BlockData blockData = b.getBlockData();
					if (blockData instanceof Rotatable) {
						Rotatable rotatable = (Rotatable) blockData;
						blockFace = rotatable.getRotation();
					}
				}
				//var directional = (Directional) b.getBlockData();
				//org.bukkit.block.data.type.Sign signBlock = (org.bukkit.block.data.type.Sign) b.getBlockData();
				//var rotation = signBlock.getRotation();
				//var rotation = BlockFace.EAST;
				element = new SignTag(name, relativeLocation, blockFace, args);
				data.add(element);
			}
		}
		if (deleteSign) {
			for (var signLocation : data.toLocations()) {
				signLocation.getBlock()
				            .setType(Material.AIR);
			}
		}
		return data;
	}

	public static SignTagData process(@NotNull final ISpatial ISpatial) {
		return process(ISpatial, PREFIX_DEFAULT, false);
	}
}
