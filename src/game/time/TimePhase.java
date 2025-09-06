package game.time;

import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Defines a period-of-day’s behavior.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public interface TimePhase {
    /**
     * Apply this phase’s effects to the given map.
     */
    void applyEffects(GameMap map);

    /**
     * @return the name of this phase (e.g. "Morning")
     */
    String getName();
}
