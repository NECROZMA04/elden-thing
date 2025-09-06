package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.ActionHistoryProvider;
import game.actions.LLMDialogueAction;
import game.interfaces.LLMDialogueBehaviour;
import game.llm.LLMMonologueManager;
import game.actors.LLMNPC;

/**
 * An abstract class that provides a base implementation for NPC dialogue behaviours
 * using a Large Language Model (LLM) to generate responses.
 * It handles the interaction with the LLMMonologueManager and provides a method
 * to interpret the raw response into an action.
 * 
 * @author Hassaan Usmani
 */
public abstract class LLMNPCDialogueBehaviour implements LLMDialogueBehaviour {

    protected final LLMMonologueManager manager;
    protected final ActionHistoryProvider historyProvider;
    protected  final LLMNPC llmnpc;

    /**
     * Constructor for LLMNPCDialogueBehaviour.
     * Initializes the behaviour with a manager for LLM monologues, a history provider,
     * and the NPC that will use this behaviour.
     *
     * @param manager The manager for LLM monologues
     * @param historyProvider The provider for action history of the player
     * @param llmnpc The NPC that will use this behaviour
     */
    protected LLMNPCDialogueBehaviour(LLMMonologueManager manager, ActionHistoryProvider historyProvider, LLMNPC llmnpc) {
        this.manager = manager;
        this.historyProvider = historyProvider;
        this.llmnpc = llmnpc;

    }

    /**
     * Interpret the raw response from the LLM and wrap it into an action.
     * This method is called after the LLMMonologueManager has generated a response.
     * 
     * @param raw
     * @param actor
     * @param map
     * @return
     */
    @Override
    public LLMDialogueAction interpretResponse(String raw, Actor actor, GameMap map) {

        return new LLMDialogueAction(raw, actor);
    }

    /**
     * Constructs the prompt for the LLM based on the NPC's dialogue context.
     * This method should be implemented by subclasses to provide specific prompts.
     *
     * @return The constructed prompt string
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        String raw = manager.requestDialogue(constructPrompt());

        return interpretResponse(raw, llmnpc, map);
    }
}
