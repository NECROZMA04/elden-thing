package game.weapons;

import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import game.actors.npc.hostile.BedOfChaos;
import game.interfaces.PlantPart;

/**
 * A leaf part of the Bed of Chaos boss.
 * Each leaf contributes a small amount of damage and
 * heals the boss by a fixed amount when grown.
 * @author Ibrahim Alfagay
 */
public class Leaf implements PlantPart {

    private static final int LEAF_DAMAGE = 1;
    private static final int HEAL_AMOUNT = 5;

    /**
     * A leaf contributes a fixed damage value.
     *
     * @return the damage points of this leaf
     */
    @Override
    public int getDamage() {
        return LEAF_DAMAGE;
    }

    /**
     * Grows this leaf by healing the boss by a fixed amount
     * and printing the updated health status.
     *
     * @param boss the BedOfChaos boss to heal
     * @param display the Display to show healing messages
     */
    @Override
    public void grow(BedOfChaos boss, Display display) {
        boss.heal(HEAL_AMOUNT);
        int currentHP = boss.getAttribute(BaseActorAttributes.HEALTH);
        int maxHP     = boss.getAttributeMaximum(BaseActorAttributes.HEALTH);
        display.println("Bed of Chaos (" + currentHP + "/" + maxHP + ") is healed");
    }
}
