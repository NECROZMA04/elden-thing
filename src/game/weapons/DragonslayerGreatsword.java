package game.weapons;

import game.attributes.ItemCapability;
import game.actions.Buyable;
import game.actors.npc.nonhostile.GoldenBeetle;
import game.actors.npc.nonhostile.merchant.Merchant;
import game.actors.npc.nonhostile.merchant.MerchantId;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;

/**
 * Dragonslayer Greatsword – 70 damage, 75 % hit.
 * Implements {@link Buyable} so it can be purchased from a Merchant.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class DragonslayerGreatsword extends WeaponItem implements Buyable {

    public DragonslayerGreatsword() {
        super("Dragonslayer Greatsword", 'D', 70, "slashes", 75);
        this.addCapability(ItemCapability.WEAPON);
    }

    /**
     * Gets the rune cost of this Dragonslayer Greatsword from the given merchant.
     *
     * @param seller the Merchant selling this item
     * @return 1500 runes if sold by Sellen, otherwise 1700 runes
     */
    @Override
    public int getPrice(Merchant seller) {
        return seller.getMerchantId() == MerchantId.SELLEN ? 1500 : 1700;
    }

    /**
     * Applies post-purchase effects for the Dragonslayer Greatsword:
     * <ul>
     *   <li>Always: +15 max health</li>
     *   <li>If sold by Sellen: spawns a {@link GoldenBeetle} adjacent to the buyer</li>
     *   <li>If sold by Kale: restores 20 stamina to the buyer</li>
     * </ul>
     *
     * @param buyer the Actor who purchased the item
     * @param seller the Merchant who sold the item
     * @param map the GameMap context (for spawning the beetle)
     */
    @Override
    public void onPurchase(Actor buyer, Merchant seller, GameMap map) {
        buyer.modifyAttributeMaximum(
                BaseActorAttributes.HEALTH,
                ActorAttributeOperations.INCREASE,
                15
        );
        if (seller.getMerchantId() == MerchantId.SELLEN) {
            Location here = map.locationOf(buyer);
            for (Exit e : here.getExits()) {
                if (!e.getDestination().containsAnActor()) {
                    map.addActor(new GoldenBeetle(), e.getDestination());
                    break;
                }
            }
        } else {
            buyer.modifyAttribute(
                    BaseActorAttributes.STAMINA,
                    ActorAttributeOperations.INCREASE,
                    20
            );
        }
    }

    /**
     * Returns the display name of this item.
     *
     * @return "Dragonslayer Greatsword"
     */
    @Override
    public String getName() {
        return "Dragonslayer Greatsword";
    }
}
