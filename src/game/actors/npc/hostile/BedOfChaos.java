package game.actors.npc.hostile;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import game.actors.NPC;
import game.attributes.ItemCapability;
import game.attributes.Status;
import game.interfaces.PlantPart;
import game.weapons.BedOfChaosIntrinsicWeapon;
import game.weapons.Branch;
import game.weapons.Leaf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Bed of Chaos boss is a stationary tree-like enemy.
 * When enemies are nearby, it attacks; otherwise, it grows
 * branches or leaves to increase its damage or heal itself.
 * @author Ibrahim Alfagay
 */
public class BedOfChaos extends NPC {

    private static final int BASE_DAMAGE = 25;
    private final Random rng = new Random();
    private final List<PlantPart> parts = new ArrayList<>();

    /**
     * Constructs the Bed of Chaos with initial HP, hostility, and
     * a placeholder intrinsic weapon (damage updated each turn).
     */
    public BedOfChaos() {
        super("Bed of Chaos", 'T', 1000);
        this.addCapability(Status.HOSTILE_TO_ENEMY);
        this.setIntrinsicWeapon(new BedOfChaosIntrinsicWeapon(0));
    }

    /**
     * Recomputes and sets the boss’s intrinsic weapon damage
     * based on the sum of all its parts’ damage.
     */
    public void updateTotalDamage() {

        int partsDamage = 0;
        for (PlantPart part : parts) {
            partsDamage += part.getDamage();
        }

        int totalDamage =  BASE_DAMAGE + partsDamage;
        this.setIntrinsicWeapon(new BedOfChaosIntrinsicWeapon(totalDamage));
    }

    /**
     * Determines which actions other actors may perform on this boss.
     *
     * @param otherActor the actor interacting with the boss
     * @param direction the map direction of the interaction
     * @param map the current game map
     * @return an ActionList of permissible actions (e.g., attacks)
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {

        // Create a list of actions
        ActionList actions = new ActionList();

        // only offer combat if the actor is hostile
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)) {

            // 1) Delegate to every item in their inventory
            for (Item item : otherActor.getItemInventory()) {
                if (item.hasCapability(ItemCapability.WEAPON)) {
                    ActionList itemActions = item.allowableActions(otherActor, map);
                    actions.add(itemActions);
                }
            }

            // 2) Still handle their intrinsic (bare‐fist) weapon
            Weapon fist = otherActor.getIntrinsicWeapon();
            if (fist != null) {
                actions.add(new AttackAction(this, direction, fist));
            }
        }
        return actions;
    }
    /**
     * Executes one turn for the boss.  Attacks if an enemy is adjacent;
     * otherwise grows a new part and applies existing parts’ growth effects.
     *
     * @param actions the list of possible actions
     * @param lastAction the last action performed by this boss
     * @param map the current game map
     * @param display the Display for printing messages
     * @return the Action chosen for this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {

        updateTotalDamage();

        for (Exit exit : map.locationOf(this).getExits()) {
            Location neighborLoc = exit.getDestination();
            if (neighborLoc.getActor() != null &&
                    neighborLoc.getActor().hasCapability(Status.HOSTILE_TO_ENEMY)) {
                return new AttackAction(neighborLoc.getActor(), exit.getName());
            }
        }

        int currentHP = this.getAttribute(BaseActorAttributes.HEALTH);
        int maxHP     = this.getAttributeMaximum(BaseActorAttributes.HEALTH);
        display.println("Bed of Chaos (" + currentHP + "/" + maxHP + ") is growing...");
        List<PlantPart> snapshot = new ArrayList<>(parts);
        boolean anyLeafHealed = false;

        PlantPart newPart;
        if (rng.nextBoolean()) {
            newPart = new Branch();
            parts.add(newPart);
            display.println("it grows a Branch...");
            newPart.onSpawn(this, display);
        }
        else {
            newPart = new Leaf();
            parts.add(newPart);
            display.println("it grows a Leaf...");
            newPart.grow(this, display);
            anyLeafHealed = true;
        }

        // Apply growth effects of existing parts
        for (PlantPart part : snapshot) {
            int hpBefore = this.getAttribute(BaseActorAttributes.HEALTH);
            part.grow(this, display);
            int hpAfter = this.getAttribute(BaseActorAttributes.HEALTH);
            if (hpAfter > hpBefore) {
                anyLeafHealed = true;
            }
        }

        if (anyLeafHealed) {
            int newHP  = this.getAttribute(BaseActorAttributes.HEALTH);
            int newMax = this.getAttributeMaximum(BaseActorAttributes.HEALTH);
            display.println("Bed of Chaos (" + newHP + "/" + newMax + ") is healed");
        }

        return new DoNothingAction();
    }

    /**
     * Heals the boss by the specified amount without exceeding max health.
     *
     * @param amount the maximum healing amount
     */
    public void heal(int amount) {
        int currentHP = this.getAttribute(BaseActorAttributes.HEALTH);
        int maxHP     = this.getAttributeMaximum(BaseActorAttributes.HEALTH);
        int toHeal    = Math.min(amount, maxHP - currentHP);
        if (toHeal > 0) {
            this.modifyAttribute(BaseActorAttributes.HEALTH,
                    ActorAttributeOperations.INCREASE,
                    toHeal);
        }
    }

    /**
     * Returns a string including the boss’s name and current/max HP.
     *
     * @return formatted name and health status
     */
    @Override
    public String toString() {
        int currentHP = this.getAttribute(BaseActorAttributes.HEALTH);
        int maxHP     = this.getAttributeMaximum(BaseActorAttributes.HEALTH);
        return name + " (" + currentHP + "/" + maxHP + " HP)";
    }
}
