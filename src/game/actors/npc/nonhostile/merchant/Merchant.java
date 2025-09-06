package game.actors.npc.nonhostile.merchant;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * An actor that can sell items/weapons to the Farmer.
 * <p>
 * The identifier returned by {@link #getMerchantId()} allows all buy–actions to
 * apply merchant-specific effects without any use of {@code instanceof}.
 * @author Muhammad Fahim Mohamed Sirajudeen
 */
public interface Merchant {

    /**
     * @return unique identifier of this merchant
     */
    MerchantId getMerchantId();

    /**
     * Convenience – gives the underlying {@link Actor}.
     */
    Actor asActor();
}
