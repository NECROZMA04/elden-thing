package game.time;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.attributes.Status;

/**
 * Represents the night phase of the game.
 * During this phase, Guts receives a damage multiplier, and other animals become peaceful.
 *
 * @author Faraz Rasool
 */
public class NightPhase implements TimePhase {

    /**
     * Applies the effects of the night phase to the game map.
     * Guts gains a damage multiplier, and Omen Sheep/Spirit Goats lose hostility.
     *
     * @param map The game map.
     */
    @Override
    public void applyEffects(GameMap map) {
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                Location location = map.at(x, y);
                if (location.containsAnActor()) {
                    Actor actor = location.getActor();
                    if (actor.hasCapability(Status.AFFECTED_BY_NIGHT)) {
                        actor.addCapability(Status.NIGHT_DAMAGE_MULTIPLIER);
                    }
                    if (actor.hasCapability(Status.BECOMES_HOSTILE)) {
                        actor.removeCapability(Status.HOSTILE_TO_FARMER);
                        actor.setIntrinsicWeapon(null);
                    }
                }
            }
        }
    }

    /**
     * Gets the name of the phase.
     *
     * @return The name of the phase, "Night".
     */
    @Override
    public String getName() {
        return "Night";
    }
}
