package game.weapons;

import game.attributes.ItemCapability;
import game.actions.Buyable;
import game.actors.npc.nonhostile.OmenSheep;
import game.actors.npc.nonhostile.merchant.Merchant;
import game.actors.npc.nonhostile.merchant.MerchantId;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;

/**
 * Katana – 50 damage, 60 % hit.
 * Implements {@link Buyable} so it can be purchased from a Merchant.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class Katana extends WeaponItem implements Buyable {

    public Katana() {
        super("Katana", 'j', 50, "slashes", 60);
        this.addCapability(ItemCapability.WEAPON);
    }

    /**
     * Gets the rune cost of this Katana. Always 500 runes (only sold by Sellen).
     *
     * @param seller the Merchant selling this item
     * @return 500 runes
     */
    @Override
    public int getPrice(Merchant seller) {
        return 500;
    }

    /**
     * Applies post-purchase effects for the Katana:
     *   Only if sold by Sellen:
     *   Buyer is harmed by 25 HP
     *   Buyer is healed by 10 HP
     *   +20 max stamina
     *   Spawns an {@link OmenSheep} adjacent to Sellen
     *
     * @param buyer the Actor who purchased the item
     * @param seller the Merchant who sold the item
     * @param map the GameMap context (for spawning the sheep)
     */
    @Override
    public void onPurchase(Actor buyer, Merchant seller, GameMap map) {
        if (seller.getMerchantId() != MerchantId.SELLEN) {
            return;
        }
        buyer.hurt(25);
        buyer.heal(10);
        buyer.modifyAttributeMaximum(
                BaseActorAttributes.STAMINA,
                ActorAttributeOperations.INCREASE,
                20
        );
        Location sellLoc = map.locationOf(seller.asActor());
        for (Exit e : sellLoc.getExits()) {
            if (!e.getDestination().containsAnActor()) {
                map.addActor(new OmenSheep(), e.getDestination());
                break;
            }
        }
    }

    /**
     * Returns the display name of this item.
     *
     * @return "Katana"
     */
    @Override
    public String getName() {
        return "Katana";
    }
}
