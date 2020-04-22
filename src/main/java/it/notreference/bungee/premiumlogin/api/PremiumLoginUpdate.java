package it.notreference.bungee.premiumlogin.api;

/**
 *
 * PremiumLogin 1.6.4 By NotReference
 *
 * @author NotReference
 * @version 1.6.4
 * @destination BungeeCord
 *
 */

/**
 *
 * @since 1.6.3
 *
 */

public class PremiumLoginUpdate {
    private boolean d;
    private String versione;

    public PremiumLoginUpdate(boolean avaliable, String version) {
        this.d = avaliable;
        this.versione = version;
    }

    public boolean isAvaliable() {

        return d;

    }

    public String getVersion() {
        return versione;
    }

}
