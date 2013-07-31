package info.bytecraft.api;

import org.bukkit.Sound;

/**
 * Represents a notification to a player
 * Sent at different occasions
 * @author Robert Catron
 */
public enum Notification {
    MESSAGE(Sound.LEVEL_UP),
    BLESS(Sound.ENDERDRAGON_WINGS),
    SUMMONED(Sound.ENDERMAN_TELEPORT),
    COMMAND_FAIL(Sound.ZOMBIE_DEATH);
    
    private final Sound sound;
    
    private Notification(Sound sound)
    {
        this.sound = sound;
    }

    /**
     * @return The sound of the notification
     */
    public Sound getSound()
    {
        return sound;
    }
}
