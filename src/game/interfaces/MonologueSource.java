package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.List;

/**
 * Represents a source of monologues for actors in the game.
 * This interface defines a method to retrieve a list of monologues
 * that an actor can deliver to a listener.
 * 
 * Implementing classes should provide the actual monologue content
 * based on the context of the game and the specific actor.
 * 
 * @author Faraz Rasool
 */
public interface MonologueSource {
    /**
     * Retrieves a list of monologues that the speaker can deliver to the listener.
     *
     * @param listener the Actor listening to the monologue
     * @param map the GameMap where the action is performed
     * @return a List of Strings representing the monologues
     */
    List<String> getMonologues(Actor listener, GameMap map);
}