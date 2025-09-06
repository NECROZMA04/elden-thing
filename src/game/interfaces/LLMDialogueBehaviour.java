package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.LLMDialogueAction;

/**
 * An interface that defines the behaviour for dialogue with a Large Language Model (LLM).
 * This interface is used to construct prompts and interpret responses from the LLM.
 * 
 * Implementing classes should provide specific logic for constructing prompts based on
 * the game context and interpreting the raw text responses from the LLM into actions.
 * 
 * @author Hassaan Usmani
 */
public interface LLMDialogueBehaviour extends Behaviour {

    /**
     * Constructs a prompt for the LLM based on the current game context.
     * This method should be implemented to provide the specific prompt that will be sent to the LLM.
     *
     * @return a String representing the prompt to be sent to the LLM
     */
    String constructPrompt();

    /**
     * Interprets the raw response from the LLM and wraps it into an action.
     * This method is called after the LLM has generated a response based on the prompt.
     *
     * @param rawResponse The raw response string from the LLM, which may contain multiple lines.
     * @param speaker The actor who is speaking the dialogue.
     * @param map The game map where the action is performed.
     * @return an instance of LLMDialogueAction that represents the dialogue action to be executed
     */
    LLMDialogueAction interpretResponse(String rawResponse, Actor speaker, GameMap map);
}
