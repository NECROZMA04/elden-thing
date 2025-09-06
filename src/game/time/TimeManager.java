package game.time;

import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Player;
import java.util.List;

/**
 * Tracks the passage of time, cycles through TimePhase instances every 4 ticks,
 * and applies their effects to the game map.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class TimeManager {
    private final List<TimePhase> phases;
    private final GameMap map;
    private int tickCount = 0;

    /**
     * Constructs a TimeManager for the given map and player.
     * Initializes the 4 phases in order.
     *
     * @param map    the GameMap on which to apply phase effects
     * @param player the Player instance to inject into MorningPhase
     */
    public TimeManager(GameMap map, Player player) {
        this.map = map;
        this.phases = List.of(
                new MorningPhase(player),
                new AfternoonPhase(),
                new EveningPhase(),
                new NightPhase()
        );
    }

    /**
     * Advances the world clock by one tick. Every 4 ticks it applies
     * the next phase’s effects.
     */
    public void tick() {
        tickCount++;
        int phaseIndex = ((tickCount - 1) / 4) % phases.size();
        phases.get(phaseIndex).applyEffects(map);
    }

    /**
     * Returns the current active phase.
     *
     * @return the TimePhase whose effects are currently in force
     */
    public TimePhase getCurrentPhase() {
        int phaseIndex = ((tickCount - 1) / 4) % phases.size();
        return phases.get(phaseIndex);
    }

    /**
     * Returns how many ticks have elapsed since the start.
     *
     * @return the total tick count
     */
    public int getTickCount() {
        return tickCount;
    }
}
