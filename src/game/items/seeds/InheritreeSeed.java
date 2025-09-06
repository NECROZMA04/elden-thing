package game.items.seeds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.attributes.GameCapability;
import game.grounds.InheritreeGround;
import game.grounds.Soil;

/**
 * Represents an Inheritree seed item in the game.
 * 
 * The InheritreeSeed can be planted by a Farmer to grow an Inheritree, which cures
 * all cursed ground (infested ground) in the surrounding area.
 * 
 * @author Hassaan Usmani
 */
public class InheritreeSeed extends Seed{

    /**
     * Constructor for the InheritreeSeed.
     * Initializes the seed with a name "Inheritree seed", a display character '*',
     * and sets it as portable.
     */
    public InheritreeSeed() {

        super("Inheritree seed", 25);
    }

    /**
     * Plants the Inheritree at the specified location and cures all infested ground
     * in the surrounding area.
     * 
     * This method decreases the Farmer's stamina by 25 points, sets the ground to
     * InheritreeGround, and cures all adjacent tiles with infested ground.
     * 
     * @param here The location where the seed is planted.
     * @param farmer The actor planting the seed.
     * @return A string describing the planting action.
     */
    @Override
    public String plantAt(Location here, Actor farmer) {

        // Decrease the Farmer's stamina by 25 points
        farmer.modifyAttribute(
                BaseActorAttributes.STAMINA,
                ActorAttributeOperations.DECREASE,
                25
        );

        // Bloom the tree
        here.setGround(new InheritreeGround());

        // Look through all adjacent tiles
        for (Exit exit : here.getExits()) {

            // Get the location of the exit
            Location adj = exit.getDestination();

            // Check if the exit location contains an infested ground
            if (adj.getGround().hasCapability(GameCapability.CURSED)) {

                // Cure the infested ground
                adj.setGround(new Soil());
            }
        }
        
        return farmer + " plants an Inheritree. Blight is removed.";
    }
}
