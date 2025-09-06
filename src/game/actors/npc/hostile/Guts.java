package game.actors.npc.hostile;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import game.actions.ListenAction;
import game.actors.NPC;
import game.attributes.ItemCapability;
import game.attributes.Status;
import game.behaviours.WanderBehaviour;
import game.interfaces.MonologueSource;
import game.weapons.BareFist;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents Guts, a hostile NPC in the game.
 * Guts is an aggressive character who attacks nearby actors and provides monologues
 * based on the state of the listener.
 * 
 * Guts has the following features:
 * - Wanders around the map using a {@link WanderBehaviour}.
 * - Attacks nearby actors if they meet certain conditions.
 * - Provides context-sensitive monologues to listeners.
 * - Uses intrinsic weapons (e.g., Bare Fists) for combat.
 * 
 * Guts implements the {@link MonologueSource} interface, allowing him to provide monologues.
 * 
 * @author Muhammad Faraz
 * Modified by: Hassaan Usmani
 */
public class Guts extends NPC implements MonologueSource {

    /**
     * Constructor for Guts.
     * Initializes Guts with a name, display character, health points, and capabilities.
     * Sets an intrinsic weapon and adds a wandering behavior.
     */
    public Guts() {
        super("Guts", 'g', 500);
        this.addCapability(Status.HOSTILE_TO_ENEMY);
        this.addCapability(Status.CAN_PROVIDE_MONOLOGUE);
        this.setIntrinsicWeapon(new BareFist());
        this.addBehaviour(new WanderBehaviour());
        this.addCapability(Status.AFFECTED_BY_NIGHT);
    }

    /**
     * Provides a list of monologues based on the listener's state.
     * 
     * @param listener The actor listening to the monologue.
     * @param map The game map where the interaction occurs.
     * @return A list of monologues that Guts can say.
     */
    @Override
    public List<String> getMonologues(Actor listener, GameMap map) {
        List<String> monologues = new ArrayList<>();

        // Check if the listener has health above 50
        if (listener.getAttribute(BaseActorAttributes.HEALTH) <= 50) {
            monologues.add("WEAK! TOO WEAK TO FIGHT ME!");
        }
        
        else {
            monologues.add("RAAAAGH!");
            monologues.add("I'LL CRUSH YOU ALL!");
        }
        
        return monologues;
    }

    /**
     * Determines Guts' actions during his turn.
     * If a nearby actor is found and meets the attack conditions, Guts attacks.
     * Otherwise, he performs a wandering action or does nothing.
     * 
     * @param actions The list of possible actions.
     * @param lastAction The last action performed by Guts.
     * @param map The game map where Guts is located.
     * @param display The display for output.
     * @return The action Guts will perform during his turn.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        
        // Check if Guts is in a location with exits
        for (Exit exit : map.locationOf(this).getExits()) {

            Location dest = exit.getDestination();

            if (dest.containsAnActor()) {

                Actor a = dest.getActor();

                if (a.getAttribute(BaseActorAttributes.HEALTH) > 50) {
                    BareFist fists = new BareFist();
                    fists.updateDamage(this.hasCapability(Status.NIGHT_DAMAGE_MULTIPLIER));
                    return new AttackAction(a, exit.getName(), fists);
                }
            }
        }
        // else wander
        Action move = behaviours.get(0).getAction(this, map);
        return move != null ? move : new DoNothingAction();
    }

    /**
     * Determines the actions that other actors can perform on Guts.
     * Includes attacking Guts or listening to his monologues if adjacent.
     * 
     * @param otherActor The actor interacting with Guts.
     * @param direction The direction of the interaction.
     * @param map The game map where the interaction occurs.
     * @return A list of allowable actions that can be performed on Guts.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();

        if (map.locationOf(this) != null && map.locationOf(otherActor) != null &&
                map.locationOf(this).getExits().stream()
                        .anyMatch(e -> e.getDestination().equals(map.locationOf(otherActor)))) {

            actions.add(new ListenAction(this, this));
        }

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
     * Returns a string representation of Guts, including his name and health.
     * 
     * @return A string representing Guts.
     */
    @Override
    public String toString() {
        return name + " (" + getAttribute(BaseActorAttributes.HEALTH) + "/" + getAttributeMaximum(BaseActorAttributes.HEALTH) + " HP)";
    }
}