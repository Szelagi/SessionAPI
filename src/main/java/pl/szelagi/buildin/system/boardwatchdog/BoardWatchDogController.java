package pl.szelagi.buildin.system.boardwatchdog;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.cause.ExceptionCause;
import pl.szelagi.util.timespigot.Time;

public class BoardWatchDogController extends Controller {
    public BoardWatchDogController(ISessionComponent sessionComponent) {
        super(sessionComponent);
    }

    @Override
    public void constructor() {
        super.constructor();
        getProcess().runControlledTaskTimer(this::stopWhenExitSpace, Time.Seconds(4), Time.Seconds(4));
    }

    public void stopWhenExitSpace() {
        var space = getSession().getCurrentBoard().getSpace();
        for (var p : getSession().getPlayers()) {
            if (!space.isLocationIn(p.getLocation())) {
                getSession().stop(new ExceptionCause("player illegal exit board space"));
                return;
            }
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "boardWatchDogController";
    }
}
