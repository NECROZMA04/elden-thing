package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for entities that can be consumed by actors
 *
 * @author Tadiwa Kennedy Vambe
 */
public interface Consumable {
    /**
     * Applies the effects of consuming this item/actor.
     * @param actor The actor consuming the item.
     * @param map The game map.
     */
    void onConsume(Actor actor, GameMap map);

    /**
     * Checks if the actor can consume this item/actor.
     * @param actor The actor attempting to consume.
     * @return True if consumable by the actor.
     */
    boolean isConsumableBy(Actor actor);

    /**
     * Provides the menu description for the consume action.
     * @param actor The actor consuming.
     * @return Menu description string.
     */
    String getMenuDescription(Actor actor);
}