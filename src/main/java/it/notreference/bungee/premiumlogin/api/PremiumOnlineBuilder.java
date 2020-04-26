package it.notreference.bungee.premiumlogin.api;

import net.md_5.bungee.api.connection.PendingConnection;


/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

public class PremiumOnlineBuilder {

    private String usr;
    private String uid;
    private boolean legacy = false;
    private String sid;

    public PremiumOnlineBuilder() {

    }

        public PremiumOnlineBuilder setUser(String userName) {
         usr = userName;
         return this;
        }

    public PremiumOnlineBuilder setUUID(String uniqueId) {
        uid = uniqueId;
        return this;
    }

    public PremiumOnlineBuilder setLegacy(boolean l) {
        legacy = l;
        return this;
    }

    public PremiumOnlineBuilder setSession(String sid) {
        this.sid = sid;
        return this;
    }

    public PremiumOnlineConnection buildConnection() {
        return new PremiumOnlineConnection(usr, legacy, uid, sid);
    }

}
