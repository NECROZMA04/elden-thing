package game.time;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.attributes.Status;
import game.weapons.AnimalRam;

/**
 * Represents the evening phase of the game.
 * During this phase, Omen Sheep and Spirit Goats become hostile to the farmer.
 *
 * @author Faraz Rasool
 */
public class EveningPhase implements TimePhase {
    private static final int ANIMAL_ATTACK_DAMAGE = 20;

    /**
     * Applies the effects of the evening phase to the game map.
     * Omen Sheep and Spirit Goats gain the HOSTILE_TO_FARMER capability and an intrinsic weapon.
     * Guts' night damage multiplier is removed.
     *
     * @param map The game map.
     */
    @Override
    public void applyEffects(GameMap map) {
        // Iterate through all map locations
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                Location location = map.at(x, y);
                if (location.containsAnActor()) {
                    Actor actor = location.getActor();
                    // OmenSheep and SpiritGoat become hostile
                    if (actor.hasCapability(Status.BECOMES_HOSTILE)) {
                        actor.addCapability(Status.HOSTILE_TO_FARMER);
                        actor.setIntrinsicWeapon(new AnimalRam(ANIMAL_ATTACK_DAMAGE, "rams"));
                    }

                    // Guts' damage multiplier is not active during the evening
                    if (actor.hasCapability(Status.AFFECTED_BY_NIGHT)) {
                        actor.removeCapability(Status.NIGHT_DAMAGE_MULTIPLIER);
                    }
                }
            }
        }
    }

    /**
     * Gets the name of the phase.
     *
     * @return The name of the phase, "Evening".
     */
    @Override
    public String getName() {
        return "Evening";
    }
}
