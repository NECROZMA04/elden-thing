package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
/**
 * Action for teleporting between locations.
 *
 * Transports an actor from their current location to a specified destination.
 * Handles map transitions and provides descriptive UI messages.
 */
public class TeleportAction extends Action {
    private final Location destination;

    /**
     * Creates a teleport action to a specific destination.
     *
     * @param destination Target location for teleportation
     */
    public TeleportAction(Location destination) {
        this.destination = destination;
    }

    /**
     * Executes the teleportation.
     *
     * <p>Moves the actor from their current map to the destination map.
     * Ensures clean transition between game locations.</p>
     *
     * @param actor The actor to teleport
     * @param map The current game map
     * @return Description of teleportation result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        map.removeActor(actor);
        destination.map().addActor(actor, destination);
        return actor + " teleports to " + destination.map();
    }

    /**
     * Provides menu description for teleportation.
     *
     * @param actor The actor performing the action
     * @return Descriptive text for UI menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Teleport to " + destination.map();
    }
}