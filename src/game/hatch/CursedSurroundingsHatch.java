package game.hatch;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.HatchStrategy;
import game.items.EggItem;
import game.util.EntityUtils;
import game.attributes.GameCapability;  // your unified enum

/**
 * A hatch strategy that allows an egg to hatch only if there are cursed surroundings nearby.
 * Implements the HatchStrategy interface.
 * 
 * @author Hassaan Usmani.
 */
public class CursedSurroundingsHatch implements HatchStrategy {

    /**
     * A method to perform any periodic updates for the hatch strategy.
     * Currently, this method does nothing.
     */
    @Override
    public void tick() { }

    /**
     * Determines if the egg can hatch based on the presence of cursed surroundings.
     *
     * @param loc The location of the egg
     * @param egg The egg item to be checked
     * @return True if there are cursed surroundings nearby, false otherwise
     */
    @Override
    public boolean canHatch(Location loc, EggItem egg) {

        return EntityUtils.hasNearbyWithCapability(loc, GameCapability.CURSED);
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
