package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.positions.GameMap;
import game.attributes.Status;
import game.interfaces.ActionHistoryProvider;
import game.ui.FancyMessage;
import game.weapons.BareFist;

import java.util.*;

/**
 * Class representing the Player in the game.
 *
 * The Player is a special type of actor controlled by the user.
 * It has attributes such as health, stamina, and runes wallet.
 * 
 * @author Adrian Kristanto
 * Modified by Hassaan Usmani
 * Modified by Muhammad Fahim Mohamed Sirajudeen
 */
public class Player extends Actor implements ActionHistoryProvider {

    /** The initial maximum stamina */
    private static final int MAX_STAMINA = 200;

    /** The Player’s rune wallet */
    private List<String> actionHistory = new ArrayList<>();
    private final BaseActorAttribute healthAttribute;
    private final BaseActorAttribute staminaAttribute;

    /**
     * Constructor to create a Player instance.
     *
     * @param name        Name to call the player in the UI
     * @param displayChar Character to represent the player in the UI
     * @param hitPoints   Player's starting number of hitpoints
     */
    public Player(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.addCapability(Status.HOSTILE_TO_ENEMY);
        this.addCapability(Status.FARMER);
        this.addCapability(Status.FOLLOWABLE);

        this.healthAttribute = new BaseActorAttribute(hitPoints);
        addAttribute(BaseActorAttributes.HEALTH, healthAttribute);

        this.staminaAttribute = new BaseActorAttribute(MAX_STAMINA);
        addAttribute(BaseActorAttributes.STAMINA, staminaAttribute);

        this.setIntrinsicWeapon(new BareFist());

    }

    @Override
    public List<String> getActionHistory() {

        return Collections.unmodifiableList(actionHistory);
    }

    /**
     * Determines the action the player will take during their turn.
     *
     * @param actions    List of possible actions the player can take
     * @param lastAction The last action performed by the player
     * @param map        The game map
     * @param display    The display to show messages to the player
     * @return The action the player chooses to perform
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // Check if the Player is conscious
        if (!this.isConscious()) {
            new Display().println(unconscious(map));
            return new DoNothingAction();
        }
        // Handle multi-turn actions
        if (lastAction.getNextAction() != null) {
            return lastAction.getNextAction();
        }
        // Show menu
        Action chosen = new Menu(actions).showMenu(this, display);
        actionHistory.add(chosen.menuDescription(this));
        return chosen;
    }

    /**
     * Handles the sequence when the player becomes unconscious (dies).
     *
     * @param actor The actor that is unconscious
     * @param map   The game map
     * @return A string message indicating the player has died
     */
    @Override
    public String unconscious(Actor actor, GameMap map) {
        new Display().println(FancyMessage.YOU_DIED);
        return super.unconscious(actor, map);
    }

    /**
     * Returns a string representation of the player, including health, stamina, and runes.
     *
     * @return A string representing the player's current state
     */
    @Override
    public String toString() {
        return name + " (" +
                this.getAttribute(BaseActorAttributes.HEALTH) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.HEALTH) + " HP, " +
                this.getAttribute(BaseActorAttributes.STAMINA) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.STAMINA) + " Stamina, " +
                "Runes: " + this.getBalance() +
                ")";
    }
}
