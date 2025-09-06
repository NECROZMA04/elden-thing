
package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import java.util.ArrayList;
import java.util.List;
/**
 * Special ground type representing a teleportation gate.
 *
 * Allows actors to travel between different locations in the game world.
 * Can be configured with multiple destination points. When an actor stands on
 * this ground, they gain access to teleport actions to all configured destinations.
 *
 * Key features:
 * - Multiple destinations supported
 * - Automatic action generation
 * - Visual representation with 'A' character
 * - Accessible to all actors
 */
public class TeleportationCircle extends Ground {
    private final List<Location> destinations = new ArrayList<>();

    /**
     * Constructs a teleportation circle.
     */
    public TeleportationCircle() {
        super('A', "Teleportation Circle");
    }

    /**
     * Adds a destination location to this teleporter.
     *
     * @param destination The location this teleporter leads to
     */
    public void addDestination(Location destination) {
        destinations.add(destination);
    }

    /**
     * Generates teleport actions for all valid destinations.
     *
     * Automatically creates TeleportAction options for each destination location
     * where no actor is currently present.
     *
     * @param actor The actor interacting with the teleporter
     * @param location The teleporter's location
     * @param direction The interaction direction
     * @return List of available teleport actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);

        if (direction.isEmpty()) { // Actor is standing on this ground
            for (Location dest : destinations) {
                if (!dest.containsAnActor()) {
                    actions.add(new TeleportAction(dest));
                }
            }
        }
        return actions;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }
}