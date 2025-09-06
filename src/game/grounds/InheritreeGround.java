package game.grounds;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import game.attributes.GameCapability;

/**
 * Represents a fully grown Inheritree in the game.
 * 
 * The Inheritree is a type of ground that heals and restores stamina
 * of any actors on adjacent tiles at the end of each turn.
 * 
 * @author Hassaan Usmani
 */
public class InheritreeGround extends Ground {

    /**
     * Constructor for the InheritreeGround.
     * Initializes the ground with a display character 't' and a name "Inheritree".
     */
    public InheritreeGround() {
        super('t', "Inheritree");
        this.addCapability(GameCapability.BLESSED_BY_GRACE);
    }

    /**
     * Executes actions at the end of each turn for the InheritreeGround.
     * 
     * This method heals and restores stamina for any actors located on tiles
     * adjacent to the Inheritree.
     * 
     * @param location The location of this InheritreeGround.
     */
    @Override
    public void tick(Location location) {

        // Look through all adjacent tiles
        for (Exit exit : location.getExits()) {

            // Get the location of the exit
            Location exitLoc = exit.getDestination();

            // Check if the exit location contains an actor
            if (exitLoc.containsAnActor()) {

                // Get the actor in the exit location and heal them
                Actor target = exitLoc.getActor();
                target.heal(5);

                // Get the actor's current stamina
                Integer currentStamina = target.getAttribute(BaseActorAttributes.STAMINA);

                // Restore stamina if the actor has a stamina attribute
                if (currentStamina != null) {

                    target.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE, 5);
                }
            }
        }
    }
}
