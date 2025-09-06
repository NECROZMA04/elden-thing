package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.GameMap;
import game.util.EntityUtils;
import game.actions.ConsumeAction;
import game.attributes.Status;
import game.interfaces.Consumable;
import game.interfaces.HatchStrategy;

import java.util.function.Supplier;

/**
 * An egg laid by an Omen Sheep that can either hatch into a new Omen Sheep or be consumed by a Farmer.
 *
 * The egg will automatically hatch after 3 turns if left on the ground. If carried by a Farmer,
 * it can be eaten to permanently increase maximum health by 10 points.
 *
 * Implements the Consumable interface to provide consumption mechanics.
 *
 * @author Ibrahim
 * Modified by: Hassaan Usmani, Tadiwa Kennedy Vambe
 */
public class EggItem extends Item implements Consumable {

    private final HatchStrategy hatchStrategy;
    private final Supplier<Actor> parent;
    private final int consumeValue;
    private final Enum<?> capabilityToChange;

    /**
     * Constructor for the Omen Sheep Egg.
     * Initializes with name 'Omen Sheep Egg', display character '0', and marks it as portable.
     */
    public EggItem(String name, char displayChar, HatchStrategy strategy, Supplier<Actor> parent, Enum<?> capabilityToChange, int consumeValue) {

        super(name, displayChar, true);
        this.hatchStrategy = strategy;
        this.parent = parent;
        this.consumeValue = consumeValue;
        this.capabilityToChange = capabilityToChange;

    }

    /**
     * Handles consumption effects when eaten by a Farmer.
     * Increases the actor's maximum health by 10 points and removes the egg from inventory.
     *
     * @param actor The actor consuming the egg
     * @param map   The game map containing the actor
     */
    @Override
    public void onConsume(Actor actor, GameMap map) {

        actor.modifyAttribute(this.capabilityToChange, ActorAttributeOperations.INCREASE, this.consumeValue);
        actor.removeItemFromInventory(this);
    }

    /**
     * Determines if an actor can consume this egg.
     * Only actors with the FARMER status (typically the player) can eat the egg.
     *
     * @param actor The actor attempting consumption
     * @return true if actor has FARMER capability, false otherwise
     */
    @Override
    public boolean isConsumableBy(Actor actor) {

        return actor.hasCapability(Status.FARMER);
    }

    /**
     * Generates menu description for consumption action.
     *
     * @param actor The actor performing the consumption
     * @return String describing the consumption action
     */
    @Override
    public String getMenuDescription(Actor actor) {
        return actor + " eats the egg";
    }

    /**
     * Retrieves the parent actor of the egg.
     *
     * @return A supplier that provides the parent actor
     */
    public Supplier<Actor> getParent() {

        return parent;
    }

    /**
     * Updates the state of the egg each turn.
     * Decrements the hatch strategy's internal counter and hatches the egg if conditions are met.
     *
     * @param currentLocation The current location of the egg
     */
    @Override
    public void tick(Location currentLocation) {

        hatchStrategy.tick();

        if (hatchStrategy.canHatch(currentLocation, this)) {

            Actor child = hatchStrategy.hatchCreature(currentLocation, this);
            Location freeLoc = EntityUtils.findFreeLocation(currentLocation);

            if (EntityUtils.locationIsFree(freeLoc)) {

                currentLocation.map().addActor(child, freeLoc);
                currentLocation.removeItem(this);
            }
        }
    }
    /**
     * Provides allowable actions for this egg.
     * Adds ConsumeAction to the action list if the interacting actor is a Farmer.
     *
     * @param actor The actor interacting with the egg
     * @param map   The game map containing the actor
     * @return List of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor actor, GameMap map) {

        ActionList actions = super.allowableActions(actor, map);
        // Only Farmers can eat the egg
        if (this.isConsumableBy(actor)) {

            actions.add(new ConsumeAction(this));
        }
        return actions;
    }
}