package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.items.EggItem;

/**
 * An interface that defines the strategy for hatching eggs into creatures.
 * Different implementations can define various conditions and mechanisms for hatching.
 * 
 * @author Hassaan Usmani.
 */
public interface HatchStrategy {

    /**
     * Updates any internal counters or state for the hatch strategy.
     * This method is called every turn.
     */
    void tick();

    /**
     * Determines if the egg is ready to hatch at the given location.
     *
     * @param loc The location of the egg
     * @param egg The egg item to be checked
     * @return True if the egg is ready to hatch, false otherwise
     */
    boolean canHatch(Location loc, EggItem egg);

    /**
     * Creates a new creature by hatching the egg.
     *
     * @param loc The location where the creature will be hatched
     * @param egg The egg item to hatch
     * @return The actor that is hatched from the egg
     */
    Actor hatchCreature(Location loc, EggItem egg);
}