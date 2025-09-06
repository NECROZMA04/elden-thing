package game.interfaces;

import edu.monash.fit2099.engine.displays.Display;
import game.actors.npc.hostile.BedOfChaos;

/**
 * Represents a part of the Bed of Chaos boss (e.g., a branch or leaf).
 * Each PlantPart can contribute damage to the boss’s attacks and
 * can “grow” each turn, potentially adding new parts or healing the boss.
 * @author Ibrahim Alfagay
 */
public interface PlantPart {

    /**
     * Calculates the total damage contributed by this part,
     * including any child parts it may have.
     *
     * @return total damage points from this part hierarchy
     */

    int getDamage();

    /**
     * Performs a growth action on this part:
     * Branches may spawn new branches or leaves.
     * Leaves heal the boss.
     *
     * @param boss the BedOfChaos instance to grow on or heal
     * @param display the Display used to print growth messages
     */
    void grow(BedOfChaos boss, Display display);

    /**
     * Called when this part is first spawned on the boss.
     * Default implementation does nothing; branches use it to trigger
     * an initial growth step.
     *
     * @param boss the BedOfChaos instance to spawn on
     * @param display the Display used to print spawn messages
     */
    default void onSpawn(BedOfChaos boss, Display display){}
}