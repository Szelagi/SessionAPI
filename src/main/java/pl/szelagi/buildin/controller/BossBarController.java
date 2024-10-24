package pl.szelagi.buildin.controller;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;

public class BossBarController extends Controller {
	private BossBar bossBar;
	private final BarColor templateBarColor;
	private final BarStyle templateBarStyle;
	private final String templateBarLabel;

	public BossBarController(ISessionComponent sessionComponent) {
		super(sessionComponent);
		this.templateBarColor = BarColor.WHITE;
		this.templateBarStyle = BarStyle.SOLID;
		this.templateBarLabel = "";
	}

	public BossBarController(ISessionComponent sessionComponent, BarColor templateBarColor, BarStyle templateBarStyle) {
		super(sessionComponent);
		this.templateBarColor = templateBarColor;
		this.templateBarStyle = templateBarStyle;
		this.templateBarLabel = "";
	}

	public BossBarController(ISessionComponent sessionComponent, BarColor templateBarColor, BarStyle templateBarStyle, String templateBarLabel) {
		super(sessionComponent);
		this.templateBarColor = templateBarColor;
		this.templateBarStyle = templateBarStyle;
		this.templateBarLabel = templateBarLabel;
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		bossBar = Bukkit.createBossBar(templateBarLabel, templateBarColor, templateBarStyle);
		bossBar.setVisible(true);
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);

		if (bossBar != null) {
			bossBar.removeAll();
			bossBar.setVisible(false);
			bossBar = null;
		}
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);

		if (bossBar != null) {
			bossBar.addPlayer(event.getPlayer());
		}
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);

		if (bossBar != null) {
			bossBar.removePlayer(event.getPlayer());
		}
	}

	public BossBar bossBar() {
		return bossBar;
	}
}
