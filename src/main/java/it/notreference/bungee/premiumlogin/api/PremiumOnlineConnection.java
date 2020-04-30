package it.notreference.bungee.premiumlogin.api;



/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class PremiumOnlineConnection {

    private String playerName;
    private boolean isLegacy;
    private String playerUUID;
    private String customSessionId;

    public PremiumOnlineConnection(String playerName, boolean isLegacy, String playerUUID, String customSessionId)
    {
        this.playerName = playerName;
        this.customSessionId = customSessionId;
        this.playerUUID = playerUUID;
        this.isLegacy = isLegacy;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    /**
     * NOTE! This returns a custom generated session id for api verifications. Not Minecraft.net session id.
     *
     * @return
     */
    public String getSessionId() {
        return customSessionId;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public boolean compare(PremiumOnlineConnection otherCon) {
        if(otherCon.getPlayerName() == playerName) {
            return false;
        }
        if(otherCon.getSessionId() == customSessionId) {
            return false;
        }
        if(otherCon.getPlayerUUID() == playerUUID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PremiumOnlineConnection{" +
                "playerName='" + playerName + '\'' +
                ", isLegacy=" + isLegacy +
                ", playerUUID='" + playerUUID + '\'' +
                ", customSessionId='" + customSessionId + '\'' +
                '}';
    }
}
