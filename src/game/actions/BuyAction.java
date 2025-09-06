package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.npc.nonhostile.merchant.Merchant;
import game.attributes.Status;

/**
 * A generic buy action for any item that is both a Buyable and an Item.
 *
 * @param <T> the concrete type of item, must extend engine’s Item and implement Buyable
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public class BuyAction<T extends Item & Buyable> extends Action {
    private final Merchant seller;
    private final T item;

    /**
     * Constructs a new BuyAction for the given seller and item.
     *
     * @param seller the merchant selling this item
     * @param item   the Buyable item to purchase
     */
    public BuyAction(Merchant seller, T item) {
        this.seller = seller;
        this.item   = item;
    }

    /**
     * Executes the purchase action.
     * Checks that the actor is allowed to buy, verifies sufficient runes,
     * deducts the price, adds the item to inventory, and applies post-purchase effects.
     *
     * @param actor the actor performing the purchase
     * @param map   the game map context
     * @return a description of the result of the action, or an empty string if not permitted
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (!actor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
            return "";
        }
        int price = item.getPrice(seller);
        if (actor.getBalance() < price) {
            return "Not enough runes.";
        }
        actor.deductBalance(price);
        actor.addItemToInventory(item);
        item.onPurchase(actor, seller, map);
        return "Purchased a " + item.getName() + ".";
    }

    /**
     * Returns the menu description for this buy action.
     *
     * @param actor the actor for whom the menu is generated
     * @return the menu string representing this purchase action
     */
    @Override
    public String menuDescription(Actor actor) {
        int price = item.getPrice(seller);
        return String.format("Buy %s (%d runes) from %s",
                item.getName(), price, seller);
    }
}
