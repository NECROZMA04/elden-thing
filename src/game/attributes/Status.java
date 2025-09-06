package game.attributes;

/**
 * Use this enum class to represent a status.
 * Example #1: if the player is sleeping, you can attack a Status.SLEEP to the player class
 * @author Riordan D. Alfredo
 */
public enum Status {
    HOSTILE_TO_ENEMY,
    FARMER,
    FOLLOWABLE,
    IMMUNE_TO_ROT, 
    CAN_PROVIDE_MONOLOGUE,
    HOSTILE_TO_FARMER,
    NIGHT_DAMAGE_MULTIPLIER,
    BECOMES_HOSTILE,
    AFFECTED_BY_NIGHT
}

