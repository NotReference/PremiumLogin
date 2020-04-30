package it.notreference.bungee.premiumlogin.api.events;


import it.notreference.bungee.premiumlogin.api.SwitchType;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;




/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class PremiumSwitchEvent extends Event implements Cancellable {

    private ProxiedPlayer player;
    private ServerInfo info;
    private SwitchType type;
    private boolean cancel = false;

    public PremiumSwitchEvent(ProxiedPlayer player, ServerInfo playerServer, SwitchType type) {
        this.player = player;
        this.info = playerServer;
        this.type = type;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public SwitchType getType() {
        return type;
    }

    public ServerInfo getPlayerServer() {
        return info;
    }

    @Override
    @Deprecated
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    @Deprecated
    public void setCancelled(boolean b) {
     cancel = b;
    }
}
