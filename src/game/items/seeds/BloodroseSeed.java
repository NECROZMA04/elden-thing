package game.items.seeds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.BloodroseGround;


/**
 * Represents a Bloodrose seed item in the game.
 * 
 * The BloodroseSeed can be planted by a Farmer to grow a Bloodrose, which saps
 * the Farmer's health by 5 HP upon planting.
 * 
 * @author Hassaan Usmani
 */
public class BloodroseSeed extends Seed{

    /**
     * Constructor for the BloodroseSeed.
     * Initializes the seed with a name "Bloodrose seed", a display character '*',
     * and sets it as portable.
     */
    public BloodroseSeed() {

        super("Bloodrose seed", 75);
    }

    /**
     * Plants the Bloodrose at the specified location and saps the Farmer's stamina and health.
     * 
     * This method sets the ground to BloodroseGround, reduces the Farmer's stamina by 75 points,
     * and decreases their health by 5 HP.
     * 
     * @param here The location where the seed is planted.
     * @param farmer The actor planting the seed.
     * @return A string describing the planting action.
     */
    @Override
    public String plantAt(Location here, Actor farmer) {

        // Decrease the Farmer's stamina by 75 points
        farmer.modifyAttribute(
                BaseActorAttributes.STAMINA,
                ActorAttributeOperations.DECREASE,
                75
        );

        // Set the ground to BloodroseGround and sap the Farmer's health
        here.setGround(new BloodroseGround());
        farmer.hurt(5);

        return farmer + " plants a Bloodrose and feels its curse.";
    }
}
