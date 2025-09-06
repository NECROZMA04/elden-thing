package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import game.attributes.GameCapability;

/**
 * A class representing a blight covering the ground of the valley.
 * @author Adrian Kristanto
 */
public class Blight extends Ground {
    public Blight() {

        super('x', "Blight");
        // Adding Infestation capability to the blight
        addCapability(GameCapability.CURSED);
    }
}
