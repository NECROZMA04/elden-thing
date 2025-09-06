package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * Class representing an intrinsic weapon called a bare fist.
 * This intrinsic weapon deals 25 damage points with a 50% chance
 * to hit the target.
 * @author Adrian Kristanto
 */
public class BareFist extends IntrinsicWeapon {
    private int baseDamage = 25;
    
    public BareFist() {
        super(25, "punches", 50);
    }

    // New method to update damage based on night mode
    public void updateDamage(boolean isNight) {
        this.damage = isNight ? 35 : baseDamage;
    }
}
