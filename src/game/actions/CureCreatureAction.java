package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.CurableCreature;

/**
 * Represents an action to cure a CurableCreature using the Talisman.
 * 
 * This action calls the cure behavior defined by the CurableCreature interface,
 * allowing the target creature to heal or undergo specific changes in the game state.
 * 
 * @author Hassaan Usmani
 */
public class CureCreatureAction extends Action {
    
    private final CurableCreature target;

    /**
     * Constructor for the CureCreatureAction.
     * 
     * @param target The CurableCreature to be cured.
     */
    public CureCreatureAction(CurableCreature target) { this.target = target; }

    /**
     * Executes the cure action on the target CurableCreature.
     * 
     * This method calls the `cureWithTalisman` method on the target, passing the actor
     * performing the action and the game map.
     * 
     * @param actor The actor performing the cure action.
     * @param map   The game map where the action is performed.
     * @return A string describing the result of the cure action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        
        return target.cureWithTalisman(actor, map);
    }

    /**
     * Returns a description of the cure action for display in the menu.
     * 
     * @param actor The actor performing the action.
     * @return A string describing the cure action.
     */
    @Override
    public String menuDescription(Actor actor) {

        return actor + " uses the Talisman on " + target;
    }
}
