package game.actors.npc.nonhostile;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import game.actions.CureCreatureAction;
import game.actors.NPC;
import game.attributes.ItemCapability;
import game.attributes.RotCapability;
import game.attributes.Status;
import game.behaviours.BehaviourStrategy;
import game.behaviours.PrioritySelectionStrategy;
import game.behaviours.WanderBehaviour;
import game.grounds.InheritreeGround;
import game.hatch.TurnBasedHatch;
import game.interfaces.CurableCreature;
import game.items.EggItem;

/**
 * Omen Sheep is a peaceful animal that roams the valley.
 * 
 * The Omen Sheep has 75 hit points and uses WanderBehaviour to move around the map.
 * It offers an AttackAction to any actor with the HOSTILE_TO_ENEMY capability.
 * Additionally, it can be cured using the Talisman, which sprouts Inheritrees
 * around its location.
 * 
 * The Omen Sheep also lays eggs every 7 turns, which hatch into new Omen Sheep.
 * 
 * This class implements the CurableCreature interface, allowing it to be cured
 * by specific items.
 * 
 * @author Hassaan Usmani
 * Modified by: Tadiwa Kennedy Vambe, Ibrahim, Muhammad Fahim, Faraz Rasool
 */
public class OmenSheep extends NPC implements CurableCreature {
    private int eggCounter = 0;

    public OmenSheep() {
        this(new PrioritySelectionStrategy());
    }
    /**
     * Constructor for OmenSheep.
     * 
     * Initializes the Omen Sheep with a name, display character, hit points, and
     * a rot countdown attribute of 15 turns.
     */

    public OmenSheep(BehaviourStrategy selectionStrategy) {
        super("Omen Sheep", 'm', 75, selectionStrategy);
        // Set the rot countdown attribute to 15 turns
        this.addAttribute(RotCapability.ROT_COUNTDOWN, new BaseActorAttribute(15));
        this.addBehaviour(new WanderBehaviour());
        this.addCapability(Status.BECOMES_HOSTILE);
    }

    /**
     * Plays the turn for the Omen Sheep.
     * 
     * Handles the rot countdown, egg-laying logic, and wandering behavior.
     * Modified to use the configured selection strategy for behaviour choice.
     *
     * @param actions    The list of possible actions.
     * @param lastAction The last action performed by the Omen Sheep.
     * @param map        The game map where the Omen Sheep is located.
     * @param display    The display used to show messages.
     * @return The action to be performed by the Omen Sheep.
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

        //logic for laying egg
        eggCounter++;

        if (eggCounter >= 7) {

            Location currentLoc = map.locationOf(this);

            if (currentLoc != null) {

                currentLoc.addItem(new EggItem(

                        "Omen Sheep Egg",
                        '0',
                        new TurnBasedHatch(3),
                        OmenSheep::new,
                        BaseActorAttributes.HEALTH,
                        10)
                );

                eggCounter = 0;
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
     * This function is adapted from the Huntsman demo given with the FIT2099 engine
     * 
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
     * Cures the Omen Sheep using the Talisman.
     * 
     * This method replaces all adjacent ground tiles with InheritreeGround.
     * 
     * @param farmer The actor using the Talisman.
     * @param map    The game map where the action is performed.
     * @return A string describing the result of the cure action.
     */
    @Override
    public String cureWithTalisman(Actor farmer, GameMap map) {

        // Location of the Omen Sheep
        Location here = map.locationOf(this);

        // Look through all adjacent tiles
        for (Exit exit: here.getExits()) {

            // Get the location of the exit
            Location adj = exit.getDestination();
            // Set the exit location contains an infested ground
            adj.setGround(new InheritreeGround());
        }

        return farmer + " sprouts Inheritrees around the sheep.";
    }

}
