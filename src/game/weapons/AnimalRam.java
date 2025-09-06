package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * An intrinsic weapon for animals that ram their opponents.
 * @author Faraz Rasool
 */
public class AnimalRam extends IntrinsicWeapon {

    /**
     * Constructor.
     * @param damage The damage it deals.
     * @param verb The verb to use for the attack.
     */
    public AnimalRam(int damage, String verb) {
        super(damage, verb);
    }
} 