package game.weapons;

import game.attributes.ItemCapability;
import game.actions.Buyable;
import game.actors.npc.nonhostile.merchant.Merchant;
import game.actors.npc.nonhostile.merchant.MerchantId;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;

/**
 * Broadsword – 30 damage, 50 % hit.
 * Implements {@link Buyable} so it can be purchased from a Merchant.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class Broadsword extends WeaponItem implements Buyable {

    public Broadsword() {
        super("Broadsword", 'b', 30, "slashes", 50);
        this.addCapability(ItemCapability.WEAPON);
    }

    /**
     * Gets the rune cost of this Broadsword from the given merchant.
     *
     * @param seller the Merchant selling this item
     * @return 100 runes if sold by Sellen, otherwise 150 runes
     */
    @Override
    public int getPrice(Merchant seller) {
        return seller.getMerchantId() == MerchantId.SELLEN ? 100 : 150;
    }

    /**
     * Applies post-purchase effects for the Broadsword:
     * heals the buyer for 10 HP and increases their maximum stat.
     * <ul>
     *   <li>If sold by Sellen: +20 max health</li>
     *   <li>If sold by Kale: +30 max stamina</li>
     * </ul>
     *
     * @param buyer the Actor who purchased the item
     * @param seller the Merchant who sold the item
     * @param map the GameMap context (not used for Broadsword)
     */
    @Override
    public void onPurchase(Actor buyer, Merchant seller, GameMap map) {
        buyer.heal(10);
        if (seller.getMerchantId() == MerchantId.SELLEN) {
            buyer.modifyAttributeMaximum(
                    BaseActorAttributes.HEALTH,
                    ActorAttributeOperations.INCREASE,
                    20
            );
        } else {
            buyer.modifyAttributeMaximum(
                    BaseActorAttributes.STAMINA,
                    ActorAttributeOperations.INCREASE,
                    30
            );
        }
    }

    /**
     * Returns the display name of this item.
     *
     * @return "Broadsword"
     */
    @Override
    public String getName() {
        return "Broadsword";
    }
}
