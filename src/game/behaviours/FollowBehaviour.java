package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Behaviour;
import game.attributes.Status;

/**
 * A class that figures out a MoveAction that will move the actor one step
 * closer to a target Actor.
 * Class adapted from mars demo application
 *
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 *
 */
public class FollowBehaviour implements Behaviour {

    private Actor target = null;

    
    @Override
    /**
     * Determines the next action for the actor to move closer to a target actor.
     * If the target is not already set, it searches for a followable actor in the surrounding exits.
     * If the target is unreachable or not on the map, it returns null.
     * Otherwise, it calculates the shortest path to the target and returns a move action.
     *
     * @param actor The actor performing the behaviour
     * @param map   The game map containing the actor and target
     * @return A MoveActorAction to move closer to the target, or null if no valid move is possible
     */
    public Action getAction(Actor actor, GameMap map) {

        Location here = map.locationOf(actor);

        if (target == null) {

            for (Exit exit : here.getExits()) {

                Actor candidate = exit.getDestination().getActor();

                if (candidate != null && candidate.hasCapability(Status.FOLLOWABLE)) {

                    target = candidate;
                }
            }
        }

        if(!map.contains(target) || !map.contains(actor))

            return null;

        Location there = map.locationOf(target);

        int currentDistance = distance(here, there);

        for (Exit exit : here.getExits()) {

            Location destination = exit.getDestination();

            if (destination.canActorEnter(actor)) {

                int newDistance = distance(destination, there);

                if (newDistance < currentDistance) {

                    return new MoveActorAction(destination, exit.getName());
                }
            }
        }

        return null;
    }

    /**
     * Compute the Manhattan distance between two locations.
     *
     * @param a the first location
     * @param b the first location
     * @return the number of steps between a and b if you only move in the four cardinal directions.
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}
