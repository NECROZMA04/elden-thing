package game.time;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Invisible actor that drives the TimeManager each turn.
 * Advances the tick, prints the current phase and turns remaining,
 * and otherwise does nothing (without spamming "does nothing").
 *
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class TimeController extends Actor {
    private final TimeManager manager;

    /**
     * Constructs a TimeController that will tick the given TimeManager.
     *
     * @param manager the TimeManager to advance each turn
     */
    public TimeController(TimeManager manager) {
        super("TimeController", ' ', 999999);
        this.manager = manager;
    }

    /**
     * Called by the engine each turn. Advances the TimeManager,
     * prints phase info to the terminal, and returns a silent no-op action.
     *
     * @param actions    unused
     * @param lastAction unused
     * @param map        the current GameMap
     * @param display    Display on which to print messages
     * @return a DoNothingAction that produces no output
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        manager.tick();
        String phaseName = manager.getCurrentPhase().getName();
        int tick = manager.getTickCount();
        int offset = (tick - 1) % 4;
        int remaining = 3 - offset;
        display.println(String.format("TimePhase: %s. Turns until next: %d", phaseName, remaining));
        return new DoNothingAction() {
            @Override
            public String execute(Actor actor, GameMap map) {
                return "";
            }
            @Override
            public String menuDescription(Actor actor) {
                return "";
            }
        };
    }
}
