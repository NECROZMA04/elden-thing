package game.behaviours;

import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is adapted from the Huntsman demo given with the FIT2099 engine
 *
 * WanderBehaviour lets an actor move to a random adjacent location each turn.
 * The behaviour scans all neighbouring exits, keeps those the actor can enter,
 * then chooses one at random.  If there are no valid moves it returns null.
 *
 * @author Riordan D. Alfredo
 * Modified by: Hassaan Usmani
 */

public class WanderBehaviour implements Behaviour {

    private final Random rand = new Random();

    /**
     * Returns a MoveAction to wander to a random location, if possible.
     * If no movement is possible, returns null.
     *
     * @param actor the Actor enacting the behaviour
     * @param map the map that actor is currently on
     * @return an Action, or null if no MoveAction is possible
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        ArrayList<Action> moves = new ArrayList<>();

        for (Exit exit : map.locationOf(actor).getExits()) {

            Location destination = exit.getDestination();

            if (destination.canActorEnter(actor)) {

                moves.add(exit.getDestination().getMoveAction(actor, "around", exit.getHotKey()));
            }
        }

        return moves.isEmpty() ? null : moves.get(rand.nextInt(moves.size()));
    }
}
