package game.actors.npc.nonhostile;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import game.actions.ConsumeAction;
import game.actors.NPC;
import game.attributes.ItemCapability;
import game.attributes.Status;
import game.behaviours.BehaviourStrategy;
import game.behaviours.FollowBehaviour;
import game.behaviours.PrioritySelectionStrategy;
import game.behaviours.WanderBehaviour;
import game.hatch.CursedSurroundingsHatch;
import game.interfaces.Consumable;
import game.items.EggItem;

/**
 * A non-hostile NPC that produces Golden Beetle Eggs periodically and can be consumed by farmers.
 * Exhibits wandering and follow behaviors. Removed from map when consumed.
 *
 * @author Tadiwa Kennedy Vambe
 * Modified by: Hassaan Usmani
 */
public class GoldenBeetle extends NPC implements Consumable {
    private int eggCounter = 0;

    public GoldenBeetle() {
        this(new PrioritySelectionStrategy());
    }

    /**
     * Constructs a Golden Beetle with default attributes and behaviors
     * Golden Beetle also immune to rot
     */
    public GoldenBeetle(BehaviourStrategy selectionStrategy) {
        super("Golden Beetle", 'b', 25, selectionStrategy);
        this.addCapability(Status.IMMUNE_TO_ROT);
        this.addBehaviour(new FollowBehaviour());
        this.addBehaviour(new WanderBehaviour());
    }

    /**
     * Applies consumption effects: heals consumer, grants 100 runes, and removes beetle from map
     * @param actor The actor consuming this beetle
     * @param map The game map containing the beetle
     */
    @Override
    public void onConsume(Actor actor, GameMap map) {
        actor.heal(15);
        actor.addBalance(1000);
        map.removeActor(this);
    }

    /**
     * Checks if actor has FARMER status
     * @param actor Actor attempting consumption
     * @return true if actor has FARMER capability
     */
    @Override
    public boolean isConsumableBy(Actor actor) {
        return actor.hasCapability(Status.FARMER);
    }

    /**
     * Provides a description of the action to consume the Golden Beetle.
     * 
     * @param actor The actor interacting with the Golden Beetle.
     * @return A string describing the consumption action.
     */
    @Override
    public String getMenuDescription(Actor actor) {
        return actor + " consumes Golden Beetle";
    }

    /**
     * Determines the Golden Beetle's actions during its turn.
     * - Priority 1: Lays a Golden Beetle Egg every 5 turns.
     * - Priority 2: Executes follow behavior if applicable.
     * - Default: Does nothing if no other actions are available.
     *
     * Modified to use the configured selection strategy for behaviour choice.
     *
     * @param actions The list of possible actions.
     * @param lastAction The last action performed by the Golden Beetle.
     * @param map The game map where the Golden Beetle is located.
     * @param display The display for output.
     * @return The action the Golden Beetle will perform during its turn.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // Priority 1: Egg laying
        eggCounter++;

        if (eggCounter >= 5) {

            Location currentLoc = map.locationOf(this);

            if (currentLoc != null) {

                currentLoc.addItem(new EggItem(
                        "Golden Beetle Egg",
                        '0',
                        new CursedSurroundingsHatch(),
                        GoldenBeetle::new,
                        BaseActorAttributes.STAMINA,
                        20
                ));

                eggCounter = 0;
                // When egg laying is done, other actions for this Golden Beetle will be skipped
                return new DoNothingAction();
            }
        }

        Action action = selectionStrategy.selectBehaviour(behaviours, this, map);

        if (action != null) return action;

        return new DoNothingAction();
    }

    /**
     * Determines the actions that other actors can perform on the Golden Beetle.
     * Includes consuming the Golden Beetle if the actor is eligible.
     * 
     * @param otherActor The actor interacting with the Golden Beetle.
     * @param direction The direction of the interaction.
     * @param map The game map where the interaction occurs.
     * @return A list of allowable actions that can be performed on the Golden Beetle.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {

        ActionList actions = super.allowableActions(otherActor, direction, map);

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

        if (this.isConsumableBy(otherActor)) {

            actions.add(new ConsumeAction(this));
        }
        return actions;
    }

    /**
     * Returns a string representation of the Golden Beetle, including its name and health.
     * 
     * @return A string representing the Golden Beetle.
     */
    @Override
    public String toString() {

        return name + " (" +
                this.getAttribute(BaseActorAttributes.HEALTH) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.HEALTH) +
                " HP)";
    }
}
