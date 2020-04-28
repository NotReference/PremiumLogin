package it.notreference.spigot.premiumlogin.utils;

import it.notreference.spigot.premiumlogin.PremiumLoginSpigot;
import it.notreference.spigot.premiumlogin.auth.AuthPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.InputStream;
import java.util.List;

/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination Spigot
 *
 */
public class PLSpigotFiles {

    private final PremiumLoginSpigot main;

    public PLSpigotFiles(PremiumLoginSpigot main) {

        this.main = main;
    }

    /**
     *
     * Writes config data.
     *
     * @param configFile
     * @return
     */
    public boolean writeConfig(File configFile) {

        try(InputStream stream = main.getResource("config_spigot.yml")) {
            Files.copy(stream, configFile.toPath());
            return true;
        } catch(Exception exc) {
        return false;
        }

    }

    /**
     *
     * Returns if this player has premium auto login.
     *
     * @param name
     * @return
     */
    public boolean hasPremiumLogin(String name) {

        if(main.getDataFile().getStringList("players") == null) {
           return false;
        }

        List<String> players = main.getDataFile().getStringList("players");
        for(String e: players) {

            if(e.equalsIgnoreCase(name)) {
                return true;
            }

        }
        return false;

    }

    /**
     *
     * Enables/disables premium. (true  = enable, false = disable)
     *
     * @param name
     * @param enableOrDisable
     */
    public void togglePremium(String name, boolean enableOrDisable) {

     if(hasPremiumLogin(name) && !enableOrDisable) {
         List<String> players = main.getDataFile().getStringList("players");
         players.remove(name);

         main.getDataFile().set("players", players);
         try {
             main.getDataFile().save(new File(main.getDataFolder(), "playersdata.yml"));
             main.getDataFile().load(new File(main.getDataFolder(),"playersdata.yml"));
         } catch (Exception e) {
             e.printStackTrace();
             main.error("Unable to togglePremium (on/off) to player " + name);
         }

     }
     if(!hasPremiumLogin(name) && enableOrDisable) {

         List<String> players = main.getDataFile().getStringList("players");
         players.add(name);

         main.getDataFile().set("players", players);
         try {
             main.getDataFile().save(new File(main.getDataFolder(), "playersdata.yml"));
             main.getDataFile().load(new File(main.getDataFolder(),"playersdata.yml"));
         } catch (Exception e) {
             e.printStackTrace();
             main.error("Unable to togglePremium (on/off) to player " + name);
         }

        }

    }

    /**
     *
     * Returns the default auth plugin
     *
     * @return
     */
    public AuthPlugin defaultAuthPlugin() {

        if(main.locklogin()) {
            return AuthPlugin.LOCKLOGIN;
        } else {
            return AuthPlugin.AUTHME;
        }

    }

    /**
     *
     * Writes the data file.
     *
     * @param file
     * @return
     */
    public boolean writeDataFile(File file) {
        try(InputStream stream = main.getResource("players_data_spigot.yml")) {
            Files.copy(stream, new File(main.getDataFolder(), "playersdata.yml").toPath());
            return true;
        } catch(Exception exc) {
            return false;
        }
    }


}
