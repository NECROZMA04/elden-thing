package game.weapons;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.*;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import java.util.Random;

/**
 * Class representing items that can be used as a weapon.
 * @author Adrian Kristanto
 * Modified by Muhammad Fahim Mohamed Sirajudeen
 */
public class WeaponItem extends Item implements Weapon {
    private static final float DEFAULT_DAMAGE_MULTIPLIER = 1.0f;
    private int damage;
    private int hitRate;
    private final String verb;
    private float damageMultiplier;

    /**
     * Constructor.
     *
     * @param name        name of the item
     * @param displayChar character to use for display when item is on the ground
     * @param damage      amount of damage this weapon does
     * @param verb        verb to use for this weapon, e.g. "hits", "zaps"
     * @param hitRate     the probability/chance to hit the target.
     */
    public WeaponItem(String name, char displayChar, int damage, String verb, int hitRate) {
        super(name, displayChar, true);
        this.damage = damage;
        this.verb = verb;
        this.hitRate = hitRate;
        this.damageMultiplier = DEFAULT_DAMAGE_MULTIPLIER;
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        Random rand = new Random();
        if (!(rand.nextInt(100) < this.hitRate)) {
            return attacker + " misses " + target + ".";
        }

        target.hurt(Math.round(damage * damageMultiplier));

        return String.format("%s %s %s for %d damage", attacker, verb, target, damage);
    }

    /**
     * Returns an ActionList containing an AttackAction against each actor
     * adjacent to the weapon’s owner.
     * <p>
     * Iterates over all exits from the owner’s current location, and if
     * the destination contains an actor, creates an AttackAction using
     * this weapon.
     *
     * @param owner the actor wielding this weapon
     * @param map the game map in which the owner and targets reside
     * @return an ActionList of AttackActions for every adjacent actor
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        // find all adjacent targets
        Location here = map.locationOf(owner);
        for (Exit exit : here.getExits()) {
            Location dest = exit.getDestination();
            if (dest.containsAnActor()) {
                // AttackAction(targetActor, directionString, thisWeapon)
                actions.add(new AttackAction(dest.getActor(),
                        exit.getName(),
                        this));
                }
            }
        return actions;
    }

}
