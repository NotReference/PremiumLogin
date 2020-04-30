package it.notreference.bungee.premiumlogin.api.events;


import it.notreference.bungee.premiumlogin.api.SwitchType;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;



/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

/*

This event will be never fired from the 1.7.1

 */

/**
 *
 * @deprecated
 *
  */
@Deprecated
public class PremiumStaffSwitchEvent extends Event implements Cancellable {

    private String player;
    private SwitchType type;
    private boolean cancel = false;

    public PremiumStaffSwitchEvent(String playerName, SwitchType type) {
        this.player = player;
        this.type = type;
    }

    public String getPlayerName() {
        return player;
    }

    public SwitchType getType() {
        return type;
    }


    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
     cancel = b;
    }
}
