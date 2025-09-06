package game.actors.npc.nonhostile.merchant;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.*;
import game.actors.NPC;
import game.attributes.ItemCapability;
import game.attributes.Status;
import game.behaviours.WanderBehaviour;
import game.interfaces.MonologueSource;
import game.weapons.Broadsword;
import game.weapons.DragonslayerGreatsword;
import game.weapons.Katana;

import java.util.Arrays;
import java.util.List;

/**
 * Represents Merchant Sellen, an itinerant trader NPC in the game.
 * Sellen is a non-hostile character who provides monologues, sells items,
 * and interacts with other actors in the game world.
 *
 * Sellen has the following features:
 * - Wanders around the map using a {@link WanderBehaviour}.
 * - Provides context-sensitive monologues based on the player's state or surroundings.
 * - Offers items for purchase, such as weapons, to eligible actors.
 * - Can be attacked by hostile actors.
 *
 * Sellen implements the {@link MonologueSource} and {@link Merchant} interfaces,
 * allowing her to provide monologues and act as a merchant.
 *
 * @author Faraz Rasool
 * Modified by: Muhammad Fahim
 */
public class Sellen extends NPC implements MonologueSource, Merchant {

    /**
     * Constructor for Merchant Sellen.
     * Initializes the merchant with a name, display character, and hit points.
     * Adds a wandering behaviour and sets the capability to provide monologues.
     */
    public Sellen() {
        super("Sellen", 's', 150);
        this.addBehaviour(new WanderBehaviour());
        this.addCapability(Status.CAN_PROVIDE_MONOLOGUE);
    }

    /* ------------------------ Monologues ------------------------ */

    /**
     * Provides a list of monologues that Sellen can say.
     * The monologues are context-sensitive and may vary based on the actor's state or surroundings.
     *
     * @param listener the Actor who is listening to Sellen
     * @param map the GameMap where the action is performed
     * @return a list of monologues that Sellen can say
     */
    @Override
    public List<String> getMonologues(Actor listener, GameMap map) {
        return Arrays.asList(
            "The academy casts out those it fears. Yet knowledge, like the stars, cannot be bound forever.",
            "You sense it too, don’t you? ? The Glintstone hums, even now."
        );
    }

    /* ------------------------ Turn behaviour -------------------- */

    /**
     * List of behaviours that the actor can perform.
     * This method iterates through the list of behaviours and returns the first non-null action.
     *
     * @param actions the list of actions available to the actor
     * @param lastAction the last action performed by the actor
     * @param map the GameMap where the action is performed
     * @param display the Display object used for output
     * @return an Action that the actor can perform
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour behaviour : behaviours) {
            Action action = behaviour.getAction(this, map);
            if (action != null) return action;
        }
        return new DoNothingAction();
    }

    /* ------------------------ Interactions ---------------------- */

    
    /**
     * This method returns a list of actions that the actor can perform.
     * It checks if the actor is within speaking range of another actor and adds appropriate actions.
     * It also checks if the interacting actor has the HOSTILE_TO_ENEMY capability
     * and adds attack actions for each weapon in the actor's inventory.
     * 
     * @param otherActor the Actor that is interacting with Sellen
     * @param direction the direction of the interaction
     * @param map the GameMap where the action is performed
     * @return a list of allowable actions for the actor
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList list = super.allowableActions(otherActor, direction, map);

        /* In speaking range? */
        if (map.locationOf(this) != null && map.locationOf(otherActor) != null &&
                map.locationOf(this).getExits().stream()
                        .anyMatch(e -> e.getDestination().equals(map.locationOf(otherActor)))) {

            /* Listen */
            if (!getMonologues(otherActor, map).isEmpty()) {
                list.add(new ListenAction(this, this));
            }

            /* Buy-options (always visible – cost a tick) */
            list.add(new BuyAction<>(this, new Broadsword()));
            list.add(new BuyAction<>(this, new DragonslayerGreatsword()));
            list.add(new BuyAction<>(this, new Katana()));


        }

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
        return MerchantId.SELLEN;
    }

    /**
     * asActor method to return the actor itself.
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
