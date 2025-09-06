package game.time;

import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Afternoon – neutral, no changes.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class AfternoonPhase implements TimePhase {
    /**
     * {@inheritDoc}
     *
     * @return the name of this phase
     */
    @Override
    public String getName() {
        return "Afternoon";
    }

    /**
     * {@inheritDoc}
     *
     * nothing happens in this Time Phase
     */
    @Override
    public void applyEffects(GameMap map) {
        // no-op
    }
}
