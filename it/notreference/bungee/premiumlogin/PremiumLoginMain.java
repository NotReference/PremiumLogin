package it.notreference.bungee.premiumlogin;

import io.github.karmaconfigs.Bungee.LockLoginBungee;
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
 * PremiumLogin 1.6 By NotReference
 * 
 * @author NotReference
 * @version 1.6
 * @destination BungeeCord
 *
 */

/**
 * 
 * @since 1.0
 * 
 */
public class PremiumLoginMain extends Plugin{

	private static PremiumLoginMain in;
	private boolean xb = false;
	private Configuration configuration;
	private Configuration players;
	private boolean locklogin = false;
	private LockLoginBungee locklog;
	private static String ver = "1.6";
	
	/**
	 * Get the locklogin api.
	 * 
	 * @return lockloginbungee api
	 */
	public LockLoginBungee getLockLogin() {
		return locklog;
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
	
	/**
	 * Get the player api (lock login)
	 * 
	 * @param the player 
	 * @return playerapi with specifed player.
	 */
	public PlayerAPI makeLockLoginAPI(ProxiedPlayer name) {
		return new PlayerAPI(locklog, name);
	}
	
	/*
	 * 
	 * When plugin enable.
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		
		/*
		 * Tassi fatti da parte.
		 * 
		 */
		if(PremiumLoginCmd.getByMessage() != "§7This server is using §bPremiumLogin " + ver + " §7by §eNotReference§7.") {
			getLogger().info("*_* Error while enabling PremiumLogin " + ver + ". Please contact me on SpigotMC.");
			return;
		} 
		
		
		/*
		 * Configuration Setup.
		 * 
		 */
		 if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "players.yml");

   
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
           	 getLogger().info("ERR - Unable to create the configuration.");
                e.printStackTrace();
            }
        }
        if (!file2.exists()) {
            try (InputStream in = getResourceAsStream("players.yml")) {
                Files.copy(in, file2.toPath());
            } catch (IOException e) {
           	 getLogger().info("ERR - Unable to create the players file.");
                e.printStackTrace();
            }
        }
        
		try {
		configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().info(exc.getMessage());
			getLogger().info("ERR - Unable to load configuration.. Try to delete it.. Disabling..");
			return;
		}
		
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().info(exc.getMessage());
			getLogger().info("ERR - Unable to load players.. Try to delete the file.. Disabling..");
			return;
		}
		
		try {
		reloadConfig();
		saveConfig();
		reloadConfig();
		} catch(Exception exc) {
			logConsole("ERR - Unable to initialize configuration.. Some operation can result bugged.");
		}
		
		/*
		 * 
		 * Configuration Setup End.
		 * 
		 */
		
		/*
		 * 
		 * 
		 */
		
		/*
		 * Blocking online mode.
		 * 
		 */
		if(getProxy().getConfig().isOnlineMode()) {
			//if(!configuration.getBoolean("allow-online")) {
			getLogger().info("WARNING - For use PremiumLogin you need to remove online-mode, or all players can autologin.");
			return;
			//}
		}
		
		/*
		 * 
		 * PremiumLock Alert.
		 * 
		 */
		if(getProxy().getPluginManager().getPlugin("PremiumLock") != null) {
			getLogger().info("WARNING - Please remove PremiumLock for prevent UUID bugs.");
		}
		
		
		/*
		 * 
		 * Enabling plugin:
		 * 
		 * - Registering commands..
		 * - Registering listeners..
		 * 
		 */
		getProxy().getPluginManager().registerListener(this, new Eventi());
		getProxy().getPluginManager().registerCommand(this, new PremiumCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumLoginCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumLookUpCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumReloadCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumAddCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumRemoveCmd());
		
		/*
		 * 
		 * Setting instance
		 * *PremiumLoginMain.i()*
		 * 
		 */
		setInstance(this);
		
		/*
		 * Plugin Messaging
		 * 
		 * Registering Channel.
		 * Compatible with all versions. (1.6-1.15.2)
		 * 
		 */
		getProxy().registerChannel("BungeeCord");
		
		
		/*
		 * 
		 * AuthMeBungee Check.
		 * 
		 */
		if(getProxy().getPluginManager().getPlugin("AuthMeBungee") != null) {
			/*
			 * 
			 * Presente.
			 * 
			 */
			xb = true;
			getLogger().info("HOOK - AuthMeBungee Found..");
		} else {
			/*
			 * Non c'è
			 * 
			 */
			xb = false;
		}
		
		/**
		 * LockLogin API Hook.
		 * @since 1.4
		 * 
		 */
		if(getProxy().getPluginManager().getPlugin("LockLogin") != null) {
			locklogin = true;
			getLogger().info("HOOK - LockLogin Found..");
			locklog = (LockLoginBungee) getProxy().getPluginManager().getPlugin("LockLogin");
		}
		
		/*
		 * ^.^
		 * 
		 */
		getLogger().info("SUCCESS - PremiumLogin " + ver + " By NotReference Enabled.");
		
	}
	
	/**
	 * 
	 * Returns if PremiumLogin is hooked into a plugin.
	 * 
	 * @param plugin name.
	 * @return .-.
	 */
	public boolean isHooked(String pluginName) {
		if(pluginName.equalsIgnoreCase("locklogin")) {
			return locklogin;
		} else if(pluginName.equalsIgnoreCase("authmebungee")) {
			return xb;
		}
		
		return false;
			
	}
	
	/*
	 * 
	 * 0_0 
	 * 
	 */
	public void onDisable() {
		getLogger().info("INFO - PremiumLogin " + ver + " By NotReference Disabled.. Byeee");
	}
	
	/*
	 * ..
	 * 
	 */
	protected void setInstance(PremiumLoginMain diocane) {
		in = diocane;
	}
	
	/*
	 * 
	 * Alias of getLogger().info(String);
	 * 
	 */
	public void logConsole(String x) {
		getLogger().info(x);
	}
	
	/**
	 * 
	 * Get the instance.
	 * 
	 * @return premiumloginmain instance.
	 */
	public static PremiumLoginMain i() {
		return in;
	}
	
	/*
	 * 
	 * Unused
	 * 
	 */
	public boolean authmebungee() {
		return xb;
	}
	
	/**
	 * 
	 * Get the player configuration.
	 * 
	 * @return player configuration.
	 */
	public Configuration getPlayersConf() {
		return players;
	}
	
	/**
	 * 
	 * Reload the player configuration.
	 * 
	 */
	public void reloadPlayerConf() {
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info("ERR - Unable to reload players configuration..");
		}
	}
	
	/**
	 * 
	 * Save the player configuration.
	 * @notes Use ConfigUtils instead.
	 * 
	 */
	public void sPlayerConf() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(players, new File(getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info("ERR - Unable to save players configuration..");
		}
	}
	
	/**
	 * 
	 * Get the configuration.
	 * 
	 * @return configuration.
	 * 
	 */
	public Configuration getConfig() {
		return configuration;
	}
	
	/**
	 * 
	 * Reload configuration.
	 * 
	 */
	public void reloadConfig() {
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			} catch(Exception exc) {
				exc.printStackTrace();
				getLogger().info("ERR - Unable to reload configuration..");
			}

	}
	
	/**
	 * 
	 * Save configuration.
	 * 
	 */
	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info("ERR - Unable to save configuration..");
		}
	}
	
}
