package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.npc.nonhostile.merchant.Merchant;

/**
 * Represents something that can be bought from a Merchant.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public interface Buyable {
    /**
     * @param seller the Merchant selling this item
     * @return the rune cost of this item from that seller
     */
    int getPrice(Merchant seller);

    /**
     * Runs any post‐purchase effects (healing, spawning monsters, stat buffs…)
     * @param buyer the Actor who bought it
     * @param seller the Merchant who sold it
     * @param map the game map (for things like spawning)
     */
    void onPurchase(Actor buyer, Merchant seller, GameMap map);

    /**
     * @return the display name (e.g. "Broadsword", "Katana")
     */
    String getName();
}
