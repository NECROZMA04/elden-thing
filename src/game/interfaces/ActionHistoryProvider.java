package game.interfaces;

import java.util.List;

/**
 * Interface for providing action history for actors in the game.
 * This interface allows actors to maintain a history of actions performed,
 * which can be useful for LLM context or other purposes.
 * 
 * Implementing classes should provide a method to retrieve the action history.
 * 
 * @author Hassaan Usmani
 */
public interface ActionHistoryProvider {
    
    /**
     * Retrieves the action history of the actor.
     * 
     * @return a List of Strings representing the actions performed by the actor
     */
    List<String> getActionHistory();
}

