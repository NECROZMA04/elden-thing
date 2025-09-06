package game.actors.npc.nonhostile;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import game.actions.CureCreatureAction;
import game.actors.NPC;
import game.attributes.GameCapability;
import game.attributes.ItemCapability;
import game.attributes.RotCapability;
import game.attributes.Status;
import game.behaviours.BehaviourStrategy;
import game.behaviours.PrioritySelectionStrategy;
import game.behaviours.WanderBehaviour;
import game.interfaces.CurableCreature;
import game.util.EntityUtils;

/**
 * Spirit Goat is a peaceful animal that roams the valley.
 * 
 * The Spirit Goat has 50 hit points and uses WanderBehaviour to move around the map.
 * It offers an AttackAction to any actor with the HOSTILE_TO_ENEMY capability.
 * Additionally, it can be cured using the Talisman, which resets its rot countdown timer.
 * The Spirit Goat itself never attacks.
 * 
 * The Spirit Goat can reproduce if there is a nearby entity with the BLESSED_BY_GRACE capability.
 * 
 * This class implements the CurableCreature interface, allowing it to be cured
 * by specific items.
 * 
 * @author Hassaan Usmani
 * Modified by Muhammad Fahim Mohamed Sirajudeen, Tadiwa Kennedy Vambe, Faraz Rasool
 */
public class SpiritGoat extends NPC implements CurableCreature {
    public SpiritGoat() {
        this(new PrioritySelectionStrategy());
    }

    /**
     * Constructor for SpiritGoat.
     * 
     * Initializes the Spirit Goat with a name, display character, hit points, and
     * a rot countdown attribute of 10 turns.
     */
    public SpiritGoat(BehaviourStrategy selectionStrategy) {
        super("Spirit Goat", 'y', 50, selectionStrategy);
        // Set the rot countdown attribute to 10 turns
        addAttribute(RotCapability.ROT_COUNTDOWN, new BaseActorAttribute(10));
        this.addBehaviour(new WanderBehaviour());
        this.addCapability(Status.BECOMES_HOSTILE);
    }

    /**
     * Plays the turn for the Spirit Goat.
     * 
     * Handles the rot countdown, reproduction logic, and behavior.
     * Modified to use the configured selection strategy for behaviour choice.
     * @param actions    The list of possible actions.
     * @param lastAction The last action performed by the Spirit Goat.
     * @param map        The game map where the Spirit Goat is located.
     * @param display    The display used to show messages.
     * @return The action to be performed by the Spirit Goat.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {

        // Get the current rot countdown
        Integer turns = getAttribute(RotCapability.ROT_COUNTDOWN);

        // Check if the rot countdown attribute is present
        if (turns != null) {

            // Decrease the rot countdown by 1
            modifyAttribute(
                    RotCapability.ROT_COUNTDOWN,
                    ActorAttributeOperations.DECREASE,
                    1
            );

            // If the countdown reaches 0, the Spirit Goat becomes unconscious
            if (getAttribute(RotCapability.ROT_COUNTDOWN) <= 0) {

                // Print the unconscious message and return a DoNothingAction
                new Display().println(this.unconscious(map));
                return new DoNothingAction();
            }
        }
        // Reproduction logic
        Location currentLocation = map.locationOf(this);

        if (EntityUtils.hasNearbyWithCapability(currentLocation, GameCapability.BLESSED_BY_GRACE)) {

            Location spawnLocation = EntityUtils.findFreeLocation(currentLocation);

            if (EntityUtils.locationIsFree(spawnLocation)) {

                map.addActor(new SpiritGoat(), spawnLocation);
            }

        }

        if (this.hasCapability(Status.HOSTILE_TO_FARMER)) {
            for (Exit exit : map.locationOf(this).getExits()) {
                Location dest = exit.getDestination();
                if (dest.containsAnActor() && dest.getActor().hasCapability(Status.FARMER)) {
                    return new AttackAction(dest.getActor(), exit.getName());
                }
            }
        }

        // Perform a wandering action
        Action a = selectionStrategy.selectBehaviour(behaviours, this, map);

        // If the wandering action is null, return a DoNothingAction
        return (a != null) ? a : new DoNothingAction();
    }

    /**
     * Determines the actions available to other actors interacting with the NPC.
     * Function adapted from the huntsman demo
     * 
     * If the interacting actor has the HOSTILE_TO_ENEMY capability, they can attack
     * the NPC. If they have a Talisman in their inventory, they can cure it.
     * 
     * @param otherActor The actor interacting with the NPC.
     * @param direction  The direction of the interaction.
     * @param map        The game map where the interaction occurs.
     * @return An ActionList containing the actions available to the interacting actor.
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

        // Look for a Talisman in the interacting actor's inventory
        for (Item item : otherActor.getItemInventory()) {

            // Check if the item is a Talisman
            if (item.getDisplayChar() == 'o') {

                // Add the CureCreatureAction to the list of actions
                actions.add(new CureCreatureAction(this));
                return actions;
            }
        }


        return actions;
    }

    /**
     * Cures the Spirit Goat using the Talisman.
     * 
     * This method resets the rot countdown timer to 10 turns.
     * 
     * @param farmer The actor using the Talisman.
     * @param map    The game map where the action is performed.
     * @return A string describing the result of the cure action.
     */
    @Override
    public String cureWithTalisman(Actor farmer, GameMap map) {

        // Reset the rot countdown to 10 turns
        modifyAttribute(
                RotCapability.ROT_COUNTDOWN,
                ActorAttributeOperations.UPDATE,
                10
        );

        return farmer + " resets the goat's rot timer.";
    }
}
