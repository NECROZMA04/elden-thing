package game.hatch;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.HatchStrategy;
import game.items.EggItem;

/**
 * A hatch strategy that allows an egg to hatch after a specified number of turns.
 * Implements the HatchStrategy interface.
 * 
 * @author Hassaan Usmani.
 */
public class TurnBasedHatch implements HatchStrategy {

    private int remainingTurns;

    /**
     * Constructor to initialize the hatch strategy with a specific number of turns.
     *
     * @param turns The number of turns required before the egg can hatch
     */
    public TurnBasedHatch(int turns) {

        this.remainingTurns = turns;
    }

    /**
     * Decrements the remaining turns counter. This method should be called every turn.
     */
    @Override
    public void tick() {

        remainingTurns--;
    }

    /**
     * Determines if the egg can hatch based on the remaining turns.
     *
     * @param loc The location of the egg
     * @param egg The egg item to be checked
     * @return True if the remaining turns are zero or less, false otherwise
     */
    @Override
    public boolean canHatch(Location loc, EggItem egg) {

        return remainingTurns <= 0;
    }

    /**
     * Hatches a creature from the egg.
     *
     * @param loc The location where the creature will be hatched
     * @param egg The egg item to hatch
     * @return The actor that is hatched from the egg
     */
    @Override
    public Actor hatchCreature(Location loc, EggItem egg) {

        return egg.getParent().get();
    }
}


