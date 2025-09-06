package game.actors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import game.attributes.RotCapability;
import game.behaviours.BehaviourStrategy;
import game.behaviours.PrioritySelectionStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for NPCs which are not hostile towards the player.
 * Modified to support different behaviour selection strategies through constructor injection.
 * @author Hassan Usmani
 * edited by: Tadiwa Kennedy Vambe
 */

public abstract class NPC extends Actor {
    protected List<Behaviour> behaviours = new ArrayList<>();
    protected final BehaviourStrategy selectionStrategy;

    /**
     * Default constructor with priority-based selection.
     */
    public NPC(String name, char displayChar, int hitPoints) {
        this(name, displayChar, hitPoints, new PrioritySelectionStrategy());
    }

    /**
     * New constructor with configurable selection strategy.
     *
     * @param selectionStrategy The behaviour selection algorithm to use
     */
    public NPC(String name, char displayChar, int hitPoints,
               BehaviourStrategy selectionStrategy) {
        super(name, displayChar, hitPoints);
        this.selectionStrategy = selectionStrategy;
    }

    /**
     * Adds a behaviour effect to the actor.
     *
     * @param behaviour the effect to add
     */
    public void addBehaviour(Behaviour behaviour) {
        behaviours.add(behaviour);
    }

    public Behaviour getBehaviour(int index) {
        return this.behaviours.get(index);
    }

    /**
     * Returns a string representation of the NPC.
     * This includes the NPC's name, current health, and remaining rot countdown.
     *
     * @return A string representation of the NPC.
     */
    @Override
    public String toString() {

        return name + " (" +
                this.getAttribute(BaseActorAttributes.HEALTH) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.HEALTH) +
                " HP, " +
                this.getAttribute(RotCapability.ROT_COUNTDOWN) + "/" +
                this.getAttributeMaximum(RotCapability.ROT_COUNTDOWN) +
                " Turns)";
    }
}
