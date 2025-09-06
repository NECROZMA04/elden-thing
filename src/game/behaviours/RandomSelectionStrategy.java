package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.List;
import java.util.Random;

/**
 * Random behaviour selection strategy.
 *
 * This strategy selects a random behaviour from the list and attempts to execute it.
 * If the selected behaviour doesn't produce a valid action, no fallback is attempted.
 *
 * @author Tadiwa Kennedy Vambe
 */
public class RandomSelectionStrategy implements BehaviourStrategy {
    private final Random random = new Random();

    /**
     * Randomly selects a behaviour and returns its action.
     *
     * @param behaviours List of behaviours to choose from
     * @param actor The actor performing the action
     * @param map The game map context
     * @return Action from randomly selected behaviour, or null if invalid
     */
    @Override
    public Action selectBehaviour(List<Behaviour> behaviours, Actor actor, GameMap map) {
        if (behaviours.isEmpty()) return null;

        int index = random.nextInt(behaviours.size());
        Behaviour selected = behaviours.get(index);
        return selected.getAction(actor, map);
    }
}