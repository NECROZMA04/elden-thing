package game.actors.npc.nonhostile;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.AttackAction;
import game.attributes.ItemCapability;
import game.attributes.Status;
import game.behaviours.WanderBehaviour;
import game.interfaces.ActionHistoryProvider;
import game.behaviours.GhostBehaviour;
import game.interfaces.LLMDialogueBehaviour;
import game.llm.LLMMonologueManager;
import game.actors.LLMNPC;
import game.util.EntityUtils;

/**
 * A non-hostile NPC that represents a ghost in the game.
 * The ghost can engage in dialogue with players and wander around the map.
 * It has a unique behaviour that allows it to interact with players, especially farmers.
 *
 * @author Hassaan Usmani
 */
public class Ghost extends LLMNPC {

    /**
     * Constructor for the Ghost NPC.
     * Initializes the ghost with a name, display character, and hit points.
     * Registers a unique behaviour for dialogue and wandering.
     *
     * @param playerProvider The provider for action history of the player
     * @param manager The manager for LLM monologues
     */
    public Ghost(ActionHistoryProvider playerProvider, LLMMonologueManager manager) {

        super("Ghost Of Whispers", 'G', 9999);

        addDialogueBehaviour(new GhostBehaviour(manager, playerProvider, this));
        addBehaviour(new WanderBehaviour());
    }

    /**
     * Runs behaviours to determine the action for the ghost.
     * This method iterates through the ghost's behaviours and returns the first non-null action.
     * 
     * @param actions The list of actions available to the ghost
     * @param lastAction The last action performed by the ghost
     * @param map The game map
     * @param display The display for the game
     * @return The action to be performed by the ghost, or a DoNothingAction if no action is available
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

    /**
     * Returns a list of actions that the ghost can perform.
     * This includes dialogue actions if the actor is a farmer.
     *
     * @param otherActor The actor interacting with the ghost
     * @param direction The direction of interaction
     * @param map The game map
     * @return A list of allowable actions for the ghost
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

        // If the other actor is a farmer, and is adjacent, add dialogue actions
        if (otherActor.hasCapability(Status.FARMER) && EntityUtils.hasNearbyWithCapability(map.locationOf(this), Status.FARMER)) {

            for (LLMDialogueBehaviour behaviour : dialogueBehaviours) {

                actions.add(behaviour.getAction(otherActor, map));
            }
        }

        return actions;
    }
}
