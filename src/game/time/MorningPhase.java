package game.time;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Player;

/**
 * Morning – a sandstorm pushes the player one tile in a random direction.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class MorningPhase implements TimePhase {
    private final Random rand = new Random();
    private final Player player;
    private final Display display = new Display();

    /**
     * @param player the one-and-only player to affect
     */
    public MorningPhase(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     *
     * @return the name of this phase
     */
    @Override
    public String getName() {
        return "Morning";
    }

    /**
     * {@inheritDoc}
     *
     * Attempts to move the player to a random adjacent location
     * if that location is free to enter. Logs the result of the
     * sandstorm to the terminal.
     *
     * @param map the GameMap on which to apply the sandstorm effect
     */
    @Override
    public void applyEffects(GameMap map) {
        // only act if player is on this map
        if (!map.contains(player)) {
            return;
        }

        Location here = map.locationOf(player);
        List<Exit> validExits = new ArrayList<>();
        for (Exit e : here.getExits()) {
            Location dest = e.getDestination();
            if (dest.canActorEnter(player)) {
                validExits.add(e);
            }
        }

        if (validExits.isEmpty()) {
            display.println("Sandstorm rages, but you cannot be moved from " + here + ".");
        } else {
            Exit chosen = validExits.get(rand.nextInt(validExits.size()));
            Location dest = chosen.getDestination();
            map.moveActor(player, dest);
            display.println("Sandstorm pushes you from " + here + " to " + dest + ".");
        }
    }
}
