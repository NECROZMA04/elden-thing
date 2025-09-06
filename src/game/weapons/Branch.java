package game.weapons;

import edu.monash.fit2099.engine.displays.Display;
import game.actors.npc.hostile.BedOfChaos;
import game.interfaces.PlantPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A tree branch part of the Bed of Chaos boss.
 * Each branch contributes a base damage, and can recursively
 * grow further branches or leaves to increase damage or healing.
 * @author Ibrahim Alfagay
 */
public class Branch implements PlantPart {

    private final List<PlantPart> children = new ArrayList<>();
    private static final int BRANCH_DAMAGE = 3;
    private static final Random RNG = new Random();

    /**
     * A branch’s total damage is its own damage plus
     * the damage of all its child parts.
     */
    @Override
    public int getDamage() {
        int total = BRANCH_DAMAGE;
        for (PlantPart child : children) {
            total += child.getDamage();
        }
        return total;
    }

    /**
     * When first spawned, immediately grow one additional layer.
     *
     * @param boss the BedOfChaos boss to grow on
     * @param display the Display to show spawn and growth messages
     */
    @Override
    public void onSpawn(BedOfChaos boss, Display display) {
        // Immediately grow one layer down
        this.grow(boss, display);
    }

    /**
     * Grows this branch by adding either a new branch or a new leaf
     * to its children, chosen randomly (50/50 chance). Prints messages
     * to the display describing the growth.
     *
     * @param boss the BedOfChaos boss this branch is attached to
     * @param display the Display to show growth messages
     */
    @Override
    public void grow(BedOfChaos boss, Display display) {
        display.println("Branch is growing...");
        boolean spawnBranch = RNG.nextBoolean();
        if (spawnBranch) {
            Branch newBranch = new Branch();
            children.add(newBranch);
            display.println("it grows a Branch...");
            newBranch.onSpawn(boss, display);
        } else {
            Leaf newLeaf = new Leaf();
            children.add(newLeaf);
            display.println("it grows a Leaf...");
            newLeaf.grow(boss, display);
        }
    }
}
