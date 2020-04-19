package it.notreference.bungee.premiumlogin;

import io.github.karmaconfigs.Bungee.API.PlayerAPI;
import it.notreference.bungee.premiumlogin.commands.PremiumAddCmd;
import it.notreference.bungee.premiumlogin.commands.PremiumCmd;
import it.notreference.bungee.premiumlogin.commands.PremiumLoginCmd;
import it.notreference.bungee.premiumlogin.commands.PremiumLookUpCmd;
import it.notreference.bungee.premiumlogin.commands.PremiumReloadCmd;
import it.notreference.bungee.premiumlogin.commands.PremiumRemoveCmd;
import it.notreference.bungee.premiumlogin.listeners.Eventi;
//import it.notreference.bungee.premiumlogin.utils.AuthUtils;






import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 *
 * PremiumLogin 1.6.2 By NotReference
 *
 * @author NotReference
 * @version 1.6.2
 * @destination BungeeCord
 *
 */

public class PremiumLoginMain extends Plugin {

	/*

	Please note that this is the 1.4 version with some modifications.

	 */

	private static PremiumLoginMain in;
	private Configuration configuration;
	private Configuration players;
	private boolean locklogin = false;
	private String ver = "1.6.2";

	/*

	Deprecated

	 */
	@Deprecated
	public PlayerAPI makeLockLoginAPI(ProxiedPlayer name) {
		return new PlayerAPI(name);
	}

	/**
	 *
	 * Get the current plugin version.
	 *
	 * @return plugin version
	 */
	public String currentVersion() {
		return ver;
	}

	@Override
	public void onEnable() {

		
		/*

		Dir setup...

		 */
		 if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "players.yml");

   /*

   Config setup..

    */

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
            	e.printStackTrace();
           	 getLogger().severe("ERR - Unable to create the configuration.");
            }
        }
        if (!file2.exists()) {
            try (InputStream in = getResourceAsStream("players.yml")) {
                Files.copy(in, file2.toPath());
            } catch (IOException e) {
				e.printStackTrace();
           	 getLogger().severe("ERR - Unable to create the players file.");

            }
        }
		
		/*

		Loading config...

		 */
		try {
		configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().severe("ERR - Unable to load the configuration.. Try to delete it.. Disabling..");
			return;
		}
		
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().severe("ERR - Unable to load players data.. Try to delete the file.. Disabling..");
			return;
		}
		
		if(getProxy().getConfig().isOnlineMode()) {
			getLogger().severe("WARNING - For use PremiumLogin you need turn off the online-mode.");
			return;
		}
		
		if(getProxy().getPluginManager().getPlugin("PremiumLock") != null) {
			getLogger().info("WARNING - Please remove PremiumLock for prevent UUID bugs.");
		}
		
		try {
		reloadConfig();
		saveConfig();
		reloadConfig();
		} catch(Exception exc) {
			logConsole("ERR - Cannot initialize configuration.. Some operation can be bugged.");
		}

		try {
			getProxy().getPluginManager().registerListener(this, new Eventi());
			getProxy().getPluginManager().registerCommand(this, new PremiumCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumLoginCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumLookUpCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumReloadCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumAddCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumRemoveCmd());
		} catch(Exception exc) {
			getLogger().severe("ERROR - Unable to enable the plugin. Please retry. Disabling...");
			return;
		}
		setInstance(this);

		/*

		BungeeCord Channel Compatible with all versions.

		 */

		getProxy().registerChannel("BungeeCord");

		
		//LockLogin API Support (1.4)
		if(getProxy().getPluginManager().getPlugin("LockLogin") != null) {
			locklogin = true;
			getLogger().info("HOOK - LockLogin Found..");
		}

		/*

		^^

		 */
		getLogger().info("SUCCESS - PremiumLogin " + ver + " By NotReference Enabled.");
		
	}

	/**
	 *
	 * Returns if PremiumLogin is hooked in a plugin.
	 *
	 * @param pluginName
	 * @return hook status
	 */
	public boolean isHooked(String pluginName) {
		if(pluginName.equalsIgnoreCase("locklogin")) {
			return locklogin;
		}
		
		return false;
			
	}
	
	public void onDisable() {

		getLogger().info("INFO - PremiumLogin 1.6.2 By NotReference Disabled. Goodbye.");
	}
	
	protected void setInstance(PremiumLoginMain main) {
		in = main;
	}

	/**
	 *
	 * Logs a string to the console.
	 *
	 * @param x
	 */
	public void logConsole(String x) {

		getLogger().info(x);
	}

	/**
	 *
	 * Returns the instance.
	 *
	 * @return
	 */
	public static PremiumLoginMain i() {

		return in;
	}

	/**
	 *
	 * Returns the player config.
	 *
	 * @return player configuration.
	 */
	public Configuration getPlayersConf() {
		return players;
	}

	/**
	 *
	 * Reloads the player config.
	 *
	 */
	public void reloadPlayerConf() {
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().severe("ERR - Cannot reload players configuration..");
		}
	}

	/**
	 *
	 * Saves the player config.
	 *
	 */
	public void sPlayerConf() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(players, new File(getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().severe("ERR - Cannot save players configuration..");
		}
	}

	/**
	 *
	 * Returns the configuration
	 *
	 * @return config
	 */
	public Configuration getConfig() {
		return configuration;
	}

	/**
	 *
	 * Reloads the configuration.
	 *
	 */
	public void reloadConfig() {
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			} catch(Exception exc) {
				exc.printStackTrace();
				getLogger().severe("ERR - Cannot reload configuration..");
			}

	}

	/**
	 *
	 * Saves the configuration.
	 *
	 */
	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().severe("ERR - Cannot save configuration..");
		}
	}
	
}
