package game.items.seeds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.PlantAction;
import game.attributes.GroundCapability;
import game.interfaces.Plantable;

/**
 * Abstract base class for plantable seeds. Defines common planting logic.
 */
public abstract class Seed extends Item implements Plantable {

    protected int requiredStamina;
    /**
     * Constructs a seed with display properties and stamina cost.
     *
     * @param name        the name of the seed
     */
    public Seed(String name, int requiredStamina) {
        super(name, '*', true);
        this.requiredStamina = requiredStamina;
    }
    /**
     * Determines the actions that can be performed with this seed by the given actor.
     *
     * If the actor is standing on a valid ground tile (e.g., soil) and has sufficient stamina,
     * the seed can be planted.
     *
     * @param actor The actor holding the seed.
     * @param map The game map where the actor is located.
     * @return A list of allowable actions for this seed.
     */
    @Override
    public ActionList allowableActions(Actor actor, GameMap map) {

        // Create a list of actions
        ActionList actions = new ActionList();
        // Get the actor's current location
        Location here = map.locationOf(actor);

        // Check if the ground is valid for planting and actor has sufficient stamina
        if (here.getGround().hasCapability(GroundCapability.PLANTABLE_AT) && actor.getAttribute(BaseActorAttributes.STAMINA) >= requiredStamina) {

            // Add the PlantAction to the list of actions
            actions.add(new PlantAction(this, this));
        }
        return actions;
    }

    /**
     * Plants the Seed at the specified location and performs the specific seed's effect.
     *
     * @param here The location where the seed is planted.
     * @param farmer The actor planting the seed.
     * @return A string describing the planting action.
     */
    public abstract String plantAt(Location here, Actor farmer);
}


