package pl.szelagi.buildin.controller;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.util.timespigot.Time;

public class BarTimerController extends Controller {
	private BossBarController bossBarController;
	private final int ticks;
	private final String template;
	private final Runnable runnable;
	private int remainingTicks;

	public BarTimerController(ISessionComponent sessionComponent, Time time, String template, @Nullable Runnable runnable) {
		super(sessionComponent);
		this.ticks = time.toTicks();
		this.template = template;
		this.runnable = runnable;
	}

	public BarTimerController(ISessionComponent sessionComponent, Time time, String template) {
		super(sessionComponent);
		this.ticks = time.toTicks();
		this.template = template;
		this.runnable = null;
	}

	public BarTimerController(ISessionComponent sessionComponent, Time time, Runnable runnable) {
		super(sessionComponent);
		this.ticks = time.toTicks();
		this.template = "Time remaining: %.2fs";
		this.runnable = runnable;
	}

	public BarTimerController(ISessionComponent sessionComponent, Time time) {
		super(sessionComponent);
		this.ticks = time.toTicks();
		this.template = "Time remaining: %.2fs";
		this.runnable = null;
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		remainingTicks = ticks;

		bossBarController = new BossBarController(this);
		bossBarController.start();

		bossBarController.bossBar()
		                 .setStyle(BarStyle.SEGMENTED_12);
		bossBarController.bossBar()
		                 .setColor(BarColor.BLUE);

		getProcess().runControlledTaskTimer(this::next, Time.Zero(), Time.Ticks(5));
	}

	private void next() {
		remainingTicks -= 5;
		if (remainingTicks <= 0) {
			if (runnable != null) {
				runnable.run();
			}
			stop();
			return;
		}
		update();
	}

	private void update() {
		if (bossBarController == null)
			return;

		var bar = bossBarController.bossBar();
		var percent = remainingTicks / (double) ticks;
		var percentNormalized = Math.min(Math.max(0, percent), 1);
		var remaining = (ticks - (ticks - remainingTicks)) / 20d;
		var formattedMessage = String.format(template, remaining);

		bar.setProgress(percentNormalized);
		bar.setTitle(formattedMessage);
	}
}
