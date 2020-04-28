package it.notreference.spigot.premiumlogin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import it.notreference.spigot.premiumlogin.auth.AuthHandler;
import it.notreference.spigot.premiumlogin.utils.PLSpigotFiles;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static org.bukkit.Bukkit.spigot;

/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination Spigot
 *
 */
public class PremiumLoginSpigot extends JavaPlugin {

    private PLSpigotFiles plspigot;
    private static PremiumLoginSpigot main;
    private boolean locklogin = false;
    private AuthHandler authHandler;
    private YamlConfiguration config;
    private YamlConfiguration dataFile;
    private String SPIGOT_URL = "https://www.spigotmc.org/resources/premiumlogin.76336/";
    private String ver = "1.7";
    private String apiUrl = "17";

    public boolean locklogin() {
        return locklogin;
    }

    public YamlConfiguration getConfig() {

        return config;

    }

    /**
     *
     * Prints an info to the console.
     *
     * @param i
     */
    public void info(String i) {
        Bukkit.getLogger().info("§6§lPremiumLogin §8» §eINFO: §7" + i);
    }

    /**
     *
     * Prints an error to the console.
     *
     * @param err
     */
    public void error(String err) {
        Bukkit.getLogger().severe("§6§lPremiumLogin §8» §cERROR: §7" + err);
    }

    /**
     *
     * Prints a warning to the console.
     *
     * @param warn
     */
    public void warn(String warn){
        Bukkit.getLogger().warning("§6§lPremiumLogin §8» §6WARNING: §7" + warn);
    }


    @Override
    public void onEnable() {

        main = this;
        authHandler = new AuthHandler(this);
        plspigot = new PLSpigotFiles(this);

        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {

             if(!getConfigManager().writeConfig(configFile)) {

                 error("Unable to create the configuration file. Please retry. Disabling..");
                 return;

             }

        }

        File playerDataFile = new File(getDataFolder(), "playersdata.yml");
        if(!playerDataFile.exists()) {

            if(!getConfigManager().writeDataFile(playerDataFile)) {

                error("Unable to create the player data file. Please retry. Disabling..");
                return;

            }

        }

        if(!getServer().getPluginManager().isPluginEnabled("MinecraftOnlineAuthenticator") || !getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
        {

            error("PremiumLoginSpigot needs dependencies to work.");
            error("Please download these plugins:");
            error("MinecraftOnlineAuthenticator - https://www.spigotmc.org/resources/minecraftonlineauthenticator.78008/");
            error("ProtocolLib - https://www.spigotmc.org/resources/protocollib.1997/");
            error("Disabling..");
            return;

        }
        try {


            config = YamlConfiguration.loadConfiguration(configFile);
            dataFile = YamlConfiguration.loadConfiguration(playerDataFile);

        } catch(Exception exc) {

            error("Unable to load the configuration or data file. Please retry. Disabling..");
            return;

        }

        if(getServer().getPluginManager().isPluginEnabled("LockLogin")) {
            locklogin = true;
            info("Hooked into LockLogin!");
        }

        if(getServer().getOnlineMode()) {

            warn("For use premiumlogin you need to disable the online mode. Disabling..");
            return;

        }

        if(spigot().getConfig().getBoolean("settings.bungeecord")) {
            warn("If you use bungeecord, you need to put this plugin in BungeeCord Plugins Folder and remove from here");
            warn("Disabling..");
            return;
        }

        getServer().getPluginCommand("premium").setExecutor(new PLComandi());
        getServer().getPluginCommand("premiumlist").setExecutor(new PLComandi());;
        getServer().getPluginCommand("premiumreload").setExecutor(new PLComandi());
        getServer().getPluginCommand("premiumlogin").setExecutor(new PLComandi());
        getServer().getPluginManager().registerEvents(new PremiumLoginSpigotListener(), this);

        info("PremiumLoginSpigot " +  ver + " by NotReference Enabled!");


    }

    /**
     *
     * Returns the player data file.
     *
     * @return
     */
    public YamlConfiguration getDataFile() {
        return dataFile;
    }

    /**
     *
     * Forces a command
     *
     * @param p
     * @param command
     */
    public void forceCommand(Player p, String command) {

        p.chat("/" + command);

    }

    /**
     *
     * Forces chat to a player.
     *
     * @param p
     * @param message
     */
    public void forceChat(Player p, String message) {

        p.chat(message);

    }

    /**
     *
     * Returns the plugin instance.
     *
     * @return
     */
    public static PremiumLoginSpigot get() {
        return main;
    }

    /**
     *
     * Returns the config manager.
     *
     * @return
     */
    public PLSpigotFiles getConfigManager() {
        return plspigot;
    }

    /**
     *
     * Returns the authentication handler. (spigot)
     *
     * @return
     */
    public AuthHandler getAuthHandler() {
        return authHandler;
    }

}
