package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import game.attributes.GroundCapability;

/**
 * A class representing the soil in the valley
 * @author Adrian Kristanto
 */
public class Soil extends Ground {
    public Soil() {

        super('.', "Soil");
        addCapability(GroundCapability.PLANTABLE_AT);
    }
}
