package game.interfaces;

import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actors.Actor;

/**
 * Represents an interface for items or objects that can be planted in the game.
 * 
 * Implementing classes define how they are planted at a specific location,
 * performing one-off effects such as curing or sapping, and replacing the ground
 * with the appropriate crop or ground type.
 * 
 * @author Hassaan Usmani
 */
public interface Plantable {
    
    /**
     * Plants the object at the specified location for the given actor.
     * 
     * This method performs any necessary effects (e.g., curing or sapping)
     * and modifies the ground at the location.
     * 
     * @param here The location where the object is planted.
     * @param farmer The actor planting the object.
     * @return A string message describing the planting action.
     */
    String plantAt(Location here, Actor farmer);
}
