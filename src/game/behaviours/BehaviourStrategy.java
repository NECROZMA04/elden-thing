package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.List;

/**
 * Strategy interface for selecting behaviours
 *
 * This interface defines a contract for different behaviour selection algorithms.
 * Implementations determine how an actor selects which behaviour to execute during its turn.
 *
 * @author Tadiwa Kennedy Vambe
 */
public interface BehaviourStrategy {
    /**
     * Strategy method for selecting behaviours from a list.
     * @param behaviours List of available behaviours
     * @param actor The actor performing the action
     * @param map The game map context
     * @return Action to perform, or null if no valid action
     */
    Action selectBehaviour(List<Behaviour> behaviours, Actor actor, GameMap map);
}