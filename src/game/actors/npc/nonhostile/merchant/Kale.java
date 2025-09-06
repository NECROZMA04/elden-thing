package game.actors.npc.nonhostile.merchant;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.*;
import game.actors.NPC;
import game.attributes.GameCapability;
import game.attributes.ItemCapability;
import game.attributes.Status;
import game.behaviours.WanderBehaviour;
import game.interfaces.MonologueSource;
import game.util.EntityUtils;
import game.weapons.Broadsword;
import game.weapons.DragonslayerGreatsword;
import java.util.ArrayList;
import java.util.List;

/**
 * Kale is a non-hostile character who provides monologues, sells items,
 * and interacts with other actors in the game world.
 * 
 * Kale has the following features:
 * - Wanders around the map using a WanderBehaviour.
 * - Provides context-sensitive monologues based on the player's state or surroundings.
 * - Offers items for purchase, such as weapons, to eligible actors.
 * - Can be attacked by hostile actors.
 * 
 * Kale implements the MonologueSource and Merchant interfaces,
 * allowing him to provide monologues and act as a merchant.
 * 
 * @author Faraz Rasool
 * Modified by: Muhammad Fahim
 */
public class Kale extends NPC implements MonologueSource, Merchant {

    /**
     * Constructor for Merchant Kale.
     * Initializes the merchant with a name, display character, and hit points.
     * Adds a wandering behaviour and sets the capability to provide monologues.
     */
    public Kale() {
        super("Merchant Kale", 'k', 200);
        this.addBehaviour(new WanderBehaviour());
        this.addCapability(Status.CAN_PROVIDE_MONOLOGUE);
    }

    /* ------------------------ Turn behaviour -------------------- */

    /**
     * List of behaviours that the actor can perform.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour behaviour : behaviours) {
            Action action = behaviour.getAction(this, map);
            if (action != null) {
                return action;
            }
        }
        return new DoNothingAction();
    }

    /* ------------------------ Helpers --------------------------- */

    /**
     * Checks if the given location is near cursed entities.
     * This is determined by checking if any of the exits from the location
     * lead to a ground with the CURSED capability.
     *
     * @param loc The location to check
     * @return true if near cursed entities, false otherwise
     */
    private boolean isNearCursedEntities(Location loc) {
        for (Exit exit : loc.getExits()) {

            if (EntityUtils.hasNearbyWithCapability(exit.getDestination(), GameCapability.CURSED)) {
                return true;
            }

        }
        return false;
    }

    /* ------------------------ Monologues ------------------------ */

    /**
     * Provides context-sensitive monologues based on the actor's state and surroundings.
     * The monologues are prioritized based on the actor's balance, inventory, and proximity to cursed entities.
     *
     * @param listener The actor listening to the monologue
     * @param map The game map where the action is performed
     * @return A list of monologues for the actor to say
     */
    @Override
    public List<String> getMonologues(Actor listener, GameMap map) {
        List<String> monologues = new ArrayList<>();
        Location location = map.locationOf(this);
        
        // Check conditions with priority
        boolean hasLowRunes = listener.getBalance() < 500;
        boolean hasEmptyInventory = listener.getItemInventory().isEmpty();
        boolean nearCursed = location != null && isNearCursedEntities(location);

        // Priority order: 1. Low runes, 2. Empty inventory, 3. Near cursed
        if (hasLowRunes) {
            monologues.add("Ah, hard times, I see. Keep your head low and your blade sharp.");
        } 
        if (hasEmptyInventory) {
            monologues.add("Not a scrap to your name? Even a farmer should carry a trinket or two.");
        }
        if (nearCursed) {
            monologues.add("Rest by the flame when you can, friend. These lands will wear you thin.");
        }
        
        // Default case
        if (monologues.isEmpty()) {
            monologues.add("A merchant's life is a lonely one. But the roads... they whisper secrets to those who listen.");
        }
        
        return monologues;
    }

    /**
     * Returns a list of actions that the actor can perform.
     * This includes attacking the actor, listening to monologues,
     * and buying items if the actor is hostile to enemies.
     * 
     * @param otherActor The actor interacting with this actor
     * @param direction The direction of the interaction
     * @param map The game map where the action is performed
     * @return A list of allowable actions for the actor
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList list = super.allowableActions(otherActor, direction, map);

        // only offer combat if the actor is hostile
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)) {

            // 1) Delegate to every item in their inventory
            for (Item item : otherActor.getItemInventory()) {
                if (item.hasCapability(ItemCapability.WEAPON)) {
                    ActionList itemActions = item.allowableActions(otherActor, map);
                    list.add(itemActions);
                }
            }

            // 2) Still handle their intrinsic (bare‐fist) weapon
            Weapon fist = otherActor.getIntrinsicWeapon();
            if (fist != null) {
                list.add(new AttackAction(this, direction, fist));
            }
        }

        if (map.locationOf(this) != null && map.locationOf(otherActor) != null &&
                map.locationOf(this).getExits().stream()
                        .anyMatch(e -> e.getDestination().equals(map.locationOf(otherActor)))) {

            if (!getMonologues(otherActor, map).isEmpty()) {
                list.add(new ListenAction(this, this));
            }

            /* Buy-options */
            list.add(new BuyAction<>(this, new Broadsword()));
            list.add(new BuyAction<>(this, new DragonslayerGreatsword()));
            /* Katana not sold here */
        }
        return list;
    }

    /* ------------------------ Merchant -------------------------- */

    /**
     * Returns the merchant ID for this actor.
     * 
     * @return The MerchantId for this actor
     */
    @Override
    public MerchantId getMerchantId() {
        return MerchantId.KALE;
    }

    /**
     * asActor returns itself.
     * 
     * @return The actor itself
     */
    @Override
    public Actor asActor() {
        return this;
    }

    /* ------------------------ Display --------------------------- */

    /**
     * String representation of the actor.
     * 
     * @return A string representation of the actor
     */
    @Override
    public String toString() {
        return name + " (" + getAttribute(BaseActorAttributes.HEALTH) + "/"
                + getAttributeMaximum(BaseActorAttributes.HEALTH) + " HP)";
    }
}
