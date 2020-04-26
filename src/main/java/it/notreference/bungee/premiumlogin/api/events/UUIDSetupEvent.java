package it.notreference.bungee.premiumlogin.api.events;

import it.notreference.bungee.premiumlogin.api.SetupMethod;
import net.md_5.bungee.api.connection.PendingConnection;
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

public class UUIDSetupEvent extends Event implements Cancellable {

    private boolean cancel = false;
    private SetupMethod metodo;
    private String newUUID;
    private String oldUUID;
    private int scope;
    private PendingConnection giocatore_con;

    public UUIDSetupEvent(SetupMethod metodo, PendingConnection playercon, String newUUID, String oldUUID, int scope) {
    this.metodo = metodo;
    this.newUUID = newUUID;
    this.oldUUID = oldUUID;
    this.scope = scope;
    this.giocatore_con = playercon;
    }

    public PendingConnection getPlayerConnection() {
        return giocatore_con;
    }

    public String getNewUUID() {
        return newUUID;
    }

    public String getOldUUID() {
        return oldUUID;
    }

    /*

    Scopes:

    0 = Used when this uuid change event is fired because the plugin applied premium skin to the player.
    1 = Used when this uuid change event is fired because the plugin tried to fix permissions plugins.
    2 = Used when this uuid change event is not specifed.

     */

    /**
     *
     * Returns the uuid change scope.
     *
     * @return 0 = skin apply on join, 1 = fix permission, 2 = not specifed
     */
    public int getScope() {
        return scope;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
      cancel = b;
    }


    /**
     *
     * Deprecated: Always returns SP.
     *
     * @return
     */

    @Deprecated
    public SetupMethod getMethod() {
        return metodo;
    }


}

