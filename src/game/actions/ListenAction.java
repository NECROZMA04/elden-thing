package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.attributes.Status;
import game.interfaces.MonologueSource;
import java.util.List;
import java.util.Random;

/**
 * Represents an action where an actor listens to another actor's monologue.
 * This action checks if the target actor has the capability to provide a monologue
 * and retrieves a random monologue from the speaker if available.
 * 
 * If the target actor cannot provide a monologue or no monologues are available,
 * a default message is returned indicating that the target has nothing to say.
 * 
 * @author Faraz Rasool
 */
public class ListenAction extends Action {
    private Actor target;
    private MonologueSource speaker;

    /**
     * Constructor.
     *
     * @param target the Actor to listen to
     * @param speaker the source of monologues
     */
    public ListenAction(Actor target, MonologueSource speaker) {
        this.target = target;
        this.speaker = speaker;
    }

    /**
     * Executes the action of listening to the target actor's monologue.
     *
     * @param actor the Actor performing the action
     * @param map the GameMap where the action is performed
     * @return a message indicating the result of the action
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (target.hasCapability(Status.CAN_PROVIDE_MONOLOGUE)) {

            List<String> monologues = speaker.getMonologues(actor, map);
            if (!monologues.isEmpty()) {
                return target + " says: \"" + monologues.get(new Random().nextInt(monologues.size())) + "\"";
            }
        }
        
        return target + " has nothing to say";
    }

    /**
     * Returns a description of the action for the menu.
     *
     * @param actor the Actor performing the action
     * @return a string describing the action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " listens to " + target;
    }
}