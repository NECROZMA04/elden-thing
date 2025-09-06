package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.List;

/**
 * Priority-based behaviour selection strategy.
 *
 * This strategy iterates through behaviours in order and selects the first valid action.
 * This maintains the original behaviour selection logic where behaviours are checked
 * in priority sequence.
 *
 * @author Tadiwa Kennedy Vambe
 */

public class PrioritySelectionStrategy implements BehaviourStrategy {
    /**
     * Selects the first valid behaviour action in priority order.
     *
     * @param behaviours List of behaviours to evaluate
     * @param actor The actor performing the action
     * @param map The game map context
     * @return First valid action found, or null if none valid
     */
    @Override
    public Action selectBehaviour(List<Behaviour> behaviours, Actor actor, GameMap map) {
        for (Behaviour behaviour : behaviours) {
            Action action = behaviour.getAction(actor, map);
            if (action != null) {
                return action;
            }
        }
        return null;
    }
}