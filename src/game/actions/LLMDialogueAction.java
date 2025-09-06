package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An Action that represents a dialogue spoken by an LLM (Large Language Model) such as Gemini.
 * This action is used to display the text returned by the LLM in the game.
 * 
 * The text can be multi-line, and it is displayed in the game's display when the action is executed.
 * 
 * @author Hassaan Usmani
 */
public class LLMDialogueAction extends Action {

    // The raw response from the LLM, which may contain multiple lines.
    private final String rawResponse;

    // The actor who is speaking the dialogue
    private final Actor speaker;

    /**
     * Constructor for LLMDialogueAction.
     *
     * @param rawResponse The raw response string from the LLM, which may contain multiple lines.
     * @param speaker The actor who is speaking the dialogue.
     */
    public LLMDialogueAction(String rawResponse, Actor speaker) {
        this.rawResponse = rawResponse;
        this.speaker = speaker;
    }

    /**
     * Executes the action, returning the raw response from the LLM.
     * The response is formatted to replace "\\n" with actual new line characters.
     *
     * @param actor The actor performing the action
     * @param map   The game map where the action is performed
     * @return The formatted response string
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        return rawResponse.replace("\\n", "\n");
    }

    /**
     * Returns a description of the action for the menu.
     * This description includes the speaker's name.
     *
     * @param actor The actor performing the action
     * @return A string describing the action
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Listen to " + speaker;
    }
}
