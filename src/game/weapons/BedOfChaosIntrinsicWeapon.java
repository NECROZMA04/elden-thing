package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * The intrinsic weapon used by the Bed of Chaos boss.
 * Represents the boss’s smash attack, with variable damage
 * and fixed accuracy.
 * @author Ibrahim Alfagay
 */
public class BedOfChaosIntrinsicWeapon extends IntrinsicWeapon {

    /**
     * Creates an intrinsic weapon for Bed of Chaos.
     *
     * @param damage the damage points of this weapon
     */
    public BedOfChaosIntrinsicWeapon(int damage) {
        super(damage, "smacks", 75);
    }
}
