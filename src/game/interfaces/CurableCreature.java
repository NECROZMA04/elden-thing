package game.interfaces;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.actors.Actor;

/**
 * Represents an interface for creatures that can be cured using the Talisman.
 * 
 * Classes implementing this interface define the behavior of how the cure action
 * is performed on the creature, including any effects or changes to the game state.
 * 
 * This interface ensures that any curable creature can interact with the Talisman
 * in a consistent manner.
 * 
 * @author Hassaan Usmani
 */
public interface CurableCreature {

    /**
     * Perform the cure action on this creature using the Talisman.
     * 
     * This method is called when a Farmer uses the Talisman on the creature.
     * It defines the effects of the cure and updates the game state accordingly.
     * 
     * @param farmer The actor using the Talisman.
     * @param map    The game map where the action is performed.
     * @return A message describing the result of the cure action.
     */
    String cureWithTalisman(Actor farmer, GameMap map);
}
