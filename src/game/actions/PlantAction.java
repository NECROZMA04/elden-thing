package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Plantable;

/**
 * Represents an action to plant a seed in the game.
 * 
 * The PlantAction removes the seed item from the actor's inventory and assigns
 * the planting process to the Plantable interface. This ensures that new plants
 * can be added without requiring changes to this class.
 * 
 * @author Hassaan Usmani
 */
public class PlantAction extends Action {

    // The seed item to be planted.
    private final Item seed;
    // The Plantable object that defines the planting behavior.
    private final Plantable planter;

    /**
     * Constructor for the PlantAction.
     * 
     * @param seed The seed item to be planted.
     * @param planter The Plantable object that defines the planting behavior.
     */
    public PlantAction(Item seed, Plantable planter) {
        
        this.seed = seed;
        this.planter = planter;
    }

    /**
     * Executes the planting action.
     * 
     * Removes the seed from the actor's inventory, determines the actor's current
     * location, and assigns the planting process to the Plantable object.
     * 
     * @param actor The actor performing the planting action.
     * @param map The game map where the action is taking place.
     * @return A string describing the result of the planting action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        // Remove the seed from the actor's inventory
        actor.removeItemFromInventory(seed);
        // Get the actor's current location
        Location here = map.locationOf(actor);
        // No instanceof or casts—just call the interface
        return planter.plantAt(here, actor);
    }

    /**
     * Returns a description of the planting action for display in the menu.
     * 
     * @param actor The actor performing the action.
     * @return A string describing the planting action.
     */
    @Override
    public String menuDescription(Actor actor) {

        return actor + " plants " + seed.toString();
    }
}
