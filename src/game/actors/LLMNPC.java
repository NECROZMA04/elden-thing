package game.actors;

import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import game.behaviours.BehaviourStrategy;
import game.interfaces.LLMDialogueBehaviour;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing a Non-Player Character (NPC) that can engage in LLM-powered dialogues.
 * This class extends the NPC class and provides functionality to manage dialogue behaviours.
 * 
 * @author Hassaan Usmani
 */
public abstract class LLMNPC extends NPC {

    protected final List<LLMDialogueBehaviour> dialogueBehaviours = new ArrayList<>();

    /**
     * Constructor to create an LLMNPC instance.
     *
     * @param name        Name to call the NPC in the UI
     * @param displayChar Character to represent the NPC in the UI
     * @param hitPoints   NPC's starting number of hitpoints
     */
    public LLMNPC(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
    }

    /**
     * Constructor to create an LLMNPC instance with a specific behaviour strategy.
     *
     * @param name        Name to call the NPC in the UI
     * @param displayChar Character to represent the NPC in the UI
     * @param hitPoints   NPC's starting number of hitpoints
     * @param strategy    The behaviour strategy for the NPC
     */
    public LLMNPC(String name, char displayChar, int hitPoints, BehaviourStrategy strategy) {
        super(name, displayChar, hitPoints, strategy);
    }

    /**
     * Adds a dialogue behaviour to the NPC.
     * This allows the NPC to engage in LLM-powered dialogues with players.
     *
     * @param b The dialogue behaviour to add
     */
    public void addDialogueBehaviour(LLMDialogueBehaviour b) {

        dialogueBehaviours.add(b);
    }

    /**
     * Returns a string representation of the NPC, including its name and health status.
     * 
     * @return A string representation of the NPC
     */
    @Override
    public String toString() {

        return name + " (" +
                this.getAttribute(BaseActorAttributes.HEALTH) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.HEALTH) +
                " HP)";
    }
}
