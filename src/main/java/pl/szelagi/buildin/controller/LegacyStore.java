package pl.szelagi.buildin.controller;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

public class LegacyStore extends Controller {
	private static Material BUTTON_MATERIAL = Material.SPRUCE_BUTTON;
	private static Material SIGN_MATERIAL = Material.SPRUCE_WALL_SIGN;
	private ItemFrame itemFrame = null;
	private final Location itemFrameLocation;
	private final Location buttonLocation;
	private final Location signLocation;
	private final BlockFace blockFace;
	private final ItemStack item;
	private final ItemStack price;

	public LegacyStore(ISessionComponent sessionComponent, BlockFace blockFace, Location location, ItemStack item, ItemStack price) {
		super(sessionComponent);
		this.blockFace = blockFace;
		this.signLocation = location.clone();
		this.itemFrameLocation = location.clone();
		this.buttonLocation = location.clone();

		this.signLocation.add(new Vector(0, 1, 0));
		this.buttonLocation.add(new Vector(0, -1, 0));

		this.item = item;
		this.price = price;
	}

	private void createSign() {
		var block = signLocation.getBlock();

		block.setType(SIGN_MATERIAL);

		var directional = (Directional) block.getBlockData();
		directional.setFacing(blockFace);
		block.setBlockData(directional);

		Sign sign = (Sign) block.getState();

		SignSide front = sign.getSide(Side.FRONT);
		var lines = front.lines();

		lines.set(0, Component.text("§1[Trade]"));
		lines.set(1, Component.text("§6§lPrice " + price.getAmount()));
		lines.set(2, Component.text(""));
		lines.set(3, Component.text("§eItem count " + item.getAmount()));

		sign.update();
	}

	private void createItemFrame() {
		itemFrame = itemFrameLocation.getWorld()
		                             .spawn(itemFrameLocation, ItemFrame.class, itemFrame -> {
			                             itemFrame.setFacingDirection(blockFace);
			                             itemFrame.setItem(item);
		                             });
	}

	private void createButton() {
		var block = buttonLocation.getBlock();
		block.setType(BUTTON_MATERIAL);
		var directional = (Directional) block.getBlockData();
		directional.setFacing(blockFace);
		block.setBlockData(directional);
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		createSign();
		createItemFrame();
		createButton();
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);
		itemFrame.remove();
		signLocation.getBlock()
		            .setType(Material.AIR);
		itemFrameLocation.getBlock()
		                 .setType(Material.AIR);
		buttonLocation.getBlock()
		              .setType(Material.AIR);
	}

	@Override
	public @Nullable Listener getListener() {
		return new InsideListener();
	}

	public boolean isButton(Block block) {
		return buttonLocation.getBlock()
		                     .equals(block);
	}

	public void buy(Player player) {
		var eq = player.getInventory();
		Audience audience = Audience.audience(player);
		if (eq.containsAtLeast(price, price.getAmount())) {
			eq.removeItem(price);
			eq.addItem(item);

			var successSound = Sound.sound(Key.key("entity.villager.trade"), Sound.Source.AMBIENT, 1f, 1f);
			audience.playSound(successSound);
			player.spawnParticle(Particle.VILLAGER_HAPPY, center(buttonLocation), 1);
		} else {
			var failedSound = Sound.sound(Key.key("entity.villager.hurt"), Sound.Source.AMBIENT, 1f, 1f);
			audience.playSound(failedSound);
			player.spawnParticle(Particle.VILLAGER_ANGRY, center(buttonLocation), 1);
		}
	}

	private static Location center(Location location) {
		return location.clone()
		               .add(0.5f, 0.5f, 0.5);
	}

	private static class InsideListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND)
				return;
			var block = event.getClickedBlock();
			if (block == null)
				return;
			if (block.getType() != BUTTON_MATERIAL)
				return;
			var session = BoardManager.getSession(event.getClickedBlock());
			if (session == null)
				return;
			var controllers = ControllerManager.getControllers(session, LegacyStore.class);
			for (var controller : controllers)
				if (controller.isButton(event.getClickedBlock())) {
					controller.buy(event.getPlayer());
					event.setCancelled(true);
					return;
				}
		}
	}
}
