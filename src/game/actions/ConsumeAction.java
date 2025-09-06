package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Consumable;

/**
 * An action that allows an actor to consume a {@link Consumable} item.
 * This action triggers the consumable's effects when executed and provides
 * appropriate menu descriptions.
 *
 * @author Tadiwa Kennedy Vambe
 */
public class ConsumeAction extends Action {
    /** The consumable item to be consumed */
    private final Consumable consumable;

    /**
     * Constructs a ConsumeAction targeting a specific consumable item.
     *
     * @param consumable The Consumable item to be consumed
     */
    public ConsumeAction(Consumable consumable) {
        this.consumable = consumable;
    }

    /**
     * Executes the consumption action by:
     * 1. Triggering the consumable's Consumable onConsume(Actor, GameMap) effect
     * 2. Returning a success message combining the consumable's menu description
     *
     * @param actor The actor performing the consumption
     * @param map   The game map containing the actor
     * @return A string describing the consumption result in the format: "[menu description] successfully"
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        consumable.onConsume(actor, map);
        return consumable.getMenuDescription(actor) + " successfully";
    }

    /**
     * Provides the menu description of this action as defined by the consumable item.
     *
     * @param actor The actor performing the action
     * @return The menu description text provided by the {@link Consumable}
     */
    @Override
    public String menuDescription(Actor actor) {
        return consumable.getMenuDescription(actor);
    }
}