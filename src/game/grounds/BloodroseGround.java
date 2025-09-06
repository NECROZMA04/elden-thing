package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents a fully grown Bloodrose in the game.
 * 
 * The Bloodrose damages any actors on adjacent tiles by 10 HP each turn.
 * If an actor becomes unconscious due to the damage, they are removed from the map.
 * If the Player is affected and dies, a "You died" message is displayed.
 * 
 * @author Hassaan Usmani
 */
public class BloodroseGround extends Ground {

    /**
     * Constructor for the BloodroseGround.
     * Initializes the ground with a display character 'w' and a name "Bloodrose".
     */
    public BloodroseGround() {

        super('w', "Bloodrose");
    }

    /**
     * Executes actions at the end of each turn for the BloodroseGround.
     * 
     * This method damages any actors located on tiles adjacent to the Bloodrose.
     * If an actor becomes unconscious, they are removed from the map.
     * If the affected actor is the Player, a "You died" message is displayed.
     * 
     * @param location The location of this BloodroseGround.
     */
    @Override
    public void tick(Location location) {

        // Look through all adjacent tiles
        for (Exit exit : location.getExits()) {

            // Get the location of the exit
            Location exitLoc = exit.getDestination();

            // Check if the exit location contains an actor
            if (exitLoc.containsAnActor()) {

                // Get the actor in the exit location and deal damage
                Actor actor = exitLoc.getActor();
                actor.hurt(10);

                // Check if the actor is unconscious
                if (!actor.isConscious()) {

                    // Print the unconscious message and remove the actor from the map
                    new Display().println(actor.unconscious(location.map()));
                }
            }
        }
    }
}
