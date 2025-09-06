package game.util;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class providing helper methods for entity-related operations.
 * Includes methods for checking capabilities nearby and finding free locations.
 *
 * Created by Hassaan Usmani.
 */
public class EntityUtils {

    /**
     * Checks if there is any ground, actor, or item with the specified capability in the nearby locations.
     *
     * @param loc        The current location to check from
     * @param capability The capability to search for
     * @return True if a nearby entity has the specified capability, false otherwise
     */
    public static boolean hasNearbyWithCapability(Location loc, Enum<?> capability) {

        for (Exit e : loc.getExits()) {

            Location dest = e.getDestination();

            if (dest.getGround().hasCapability(capability)) {

                return true;
            }

            Actor a = dest.getActor();

            if (a != null && a.hasCapability(capability)) {

                return true;
            }

            for (Item i : dest.getItems()) {

                if (i.hasCapability(capability)) {

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds a free location for an actor to enter, starting from the current location.
     * If the current location is free, it is returned. Otherwise, it searches nearby exits.
     *
     * @param currentLocation The current location to start the search
     * @return A free location that an actor can enter, or the current location if no free location is found
     */
    public static Location findFreeLocation(Location currentLocation) {

        if (locationIsFree(currentLocation)) {

            return currentLocation;
        }

        List<Exit> exits = new ArrayList<>(currentLocation.getExits());
        Collections.shuffle(exits);

        for (Exit exit : exits) {

            Location dest = exit.getDestination();

            if (locationIsFree(dest)) {

                return dest;
            }
        }

        return currentLocation;
    }

    /**
     * Checks if the specified location is free for an actor to enter.
     * A location is considered free if it can be entered by an actor and does not contain any actors.
     *
     * @param currentLocation The location to check
     * @return True if the location is free, false otherwise
     */
    public static boolean locationIsFree(Location currentLocation) {

        return currentLocation.canActorEnter(null) && !currentLocation.containsAnActor();
    }
}
