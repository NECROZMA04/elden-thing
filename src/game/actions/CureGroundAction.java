package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Soil;

/**
 * Represents an action to cure an infested ground tile using the Talisman.
 * 
 * This action allows an actor to spend stamina to cure a specific ground tile,
 * transforming it into healthy soil.
 * 
 * @author Hassaan Usmani
 */
public class CureGroundAction extends Action {

    private final Location location;

    /**
     * Constructor for the CureGroundAction.
     * 
     * @param location The location of the ground tile to be cured.
     */
    public CureGroundAction(Location location) {this.location = location;}

    /**
     * Executes the cure action on the specified ground tile.
     * 
     * This method decreases the actor's stamina by 50 points and transforms the
     * ground at the specified location into healthy soil.
     * 
     * @param actor The actor performing the cure action.
     * @param map   The game map where the action is performed.
     * @return A string describing the result of the cure action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        // Spend 50 stamina
        actor.modifyAttribute(
                BaseActorAttributes.STAMINA,
                ActorAttributeOperations.DECREASE,
                50
        );

        // Cure the tile
        location.setGround(new Soil());

        return actor + " cures the soil with the Talisman.";
    }

    /**
     * Returns a description of the cure action for display in the menu.
     * 
     * @param actor The actor performing the action.
     * @return A string describing the cure action.
     */
    @Override
    public String menuDescription(Actor actor) {

        return actor + " uses Talisman to cure soil";
    }
}
