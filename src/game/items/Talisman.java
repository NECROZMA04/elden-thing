package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.attributes.GameCapability;
import game.actions.CureGroundAction;

/**
 * A class representing a Talisman that can cure soil or creatures.
 * 
 * The Talisman allows an actor to cure infested ground tiles or curable creatures
 * by performing specific actions. Curing ground requires 50 stamina points.
 * 
 * @author Hassaan Usmani
 */
public class Talisman extends Item {

    /**
     * Constructor for the Talisman.
     * 
     * Initializes the Talisman with a name "Talisman", a display character 'o',
     * and sets it as portable.
     */
    public Talisman() {

        super("Talisman", 'o', true);
    }

    /**
     * Determines the actions that can be performed with the Talisman by the given actor.
     * 
     * If the actor is standing on infested ground and has sufficient stamina, the ground
     * can be cured. Additional actions may be added for curing creatures.
     * 
     * @param actor The actor holding the Talisman.
     * @param map   The game map where the actor is located.
     * @return A list of allowable actions for the Talisman.
     */
    @Override
    public ActionList allowableActions(Actor actor, GameMap map) {

        // Create a list of actions
        ActionList actions = new ActionList();
        // Get the actor's current location
        Location here = map.locationOf(actor);

        // Check if the ground is infested and actor has sufficient stamina
        if (here.getGround().hasCapability(GameCapability.CURSED) && actor.getAttribute(BaseActorAttributes.STAMINA) >= 50) {

            // Add the CureGroundAction to the list of actions
            actions.add(new CureGroundAction(here));
        }

        return actions;
    }
}
