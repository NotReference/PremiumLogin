package it.notreference.bungee.premiumlogin.utils.data;



/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class DefaultPlayerData implements PlayerData
{

    private final long id;
    private final String name;
    private final String lastUuid;
    private final boolean registeredA;
    private final boolean registeredL;
    private final boolean premium;
    private PlayerDataHandler handler;

    public DefaultPlayerData(long id, String name, String lastUuid, boolean regAuthme, boolean regLockLogin, boolean premium, PlayerDataHandler handler) {
       this.id = id;
       this.name = name;
       this.lastUuid = lastUuid;
       this.registeredA  = regAuthme;
       this.registeredL = regLockLogin;
       this.premium = premium;
       this.handler = handler;
    }

    @Override
    public void saveData() {
    handler.saveAllDatas();
    }

    @Override
    public void clearData() {
    handler.removeDataWhere(id);
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public boolean isPremium() {
        return premium;
    }

    @Override
    public String getLastUsedUUID() {
        return lastUuid;
    }

    @Override
    public long getPlayerId() {
        return id;
    }

    @Override
    public boolean isRegistered(String plugin) {
        if(plugin.equalsIgnoreCase("locklogin")) {
            return registeredL;
        } else if(plugin.equalsIgnoreCase("authme")) {
            return registeredA;
        } else {
            return false;
        }
    }
}
