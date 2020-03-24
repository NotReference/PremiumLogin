package it.notreference.bungee.premiumlogin;

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

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * PremiumLogin 1.3 by NotReference
 *
 * @class Main class
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PremiumLoginMain extends Plugin{

	private static PremiumLoginMain in;
	private boolean xb = false;
	private Configuration configuration;
	private Configuration players;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		
		
		if(PremiumLoginCmd.getByMessage() != "§7This server is using §bPremiumLogin 1.3 §7by §eNotReference§7.") {
			getLogger().info("MERDONE - Brutto skidder di merda fottiti, ora non mi abiliterò.");
			getLogger().info("TASSINELLO - You are a fucking skidder, fuck you; now i will not enable.");
			return;
		} 
		
		
		//Config
		 if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "players.yml");

   
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
           	 getLogger().info("ERR - Cannot create the configuration.");
                e.printStackTrace();
            }
        }
        if (!file2.exists()) {
            try (InputStream in = getResourceAsStream("players.yml")) {
                Files.copy(in, file2.toPath());
            } catch (IOException e) {
           	 getLogger().info("ERR - Cannot create the players file.");
                e.printStackTrace();
            }
        }
		
		//Config.
		try {
		configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().info(exc.getMessage());
			getLogger().info("ERR - Cannot load configuration.. Try to delete it.. Disabling..");
			return;
		}
		
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().info(exc.getMessage());
			getLogger().info("ERR - Cannot load players.. Try to delete the file.. Disabling..");
			return;
		}
		
		if(getProxy().getConfig().isOnlineMode()) {
			//if(!configuration.getBoolean("allow-online")) {
			getLogger().info("WARNING - For use PremiumLogin you need to remove online-mode, or all players can autologin. If you want continue set allow-online: to true");
			return;
			//}
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
		
		//logConsole("CONFIG - Reading and loading configuration..");
		
		getProxy().getPluginManager().registerListener(this, new Eventi());
		getProxy().getPluginManager().registerCommand(this, new PremiumCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumLoginCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumLookUpCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumReloadCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumAddCmd());
		getProxy().getPluginManager().registerCommand(this, new PremiumRemoveCmd());
		
		setInstance(this);
		//Registriamo il canale bungeecord compatibile anche con 1.13+
		getProxy().registerChannel("BungeeCord");
		
		
		//Controlliamo se è presente AuthMeBungee.
		if(getProxy().getPluginManager().getPlugin("AuthMeBungee") != null) {
		 	//authmebungee -> si
			xb = true;
			getLogger().info("HOOK - AuthMeBungee Found..");
		} else {
			//non c'è
			xb = false;
		}
		
		getLogger().info("SUCCESS - PremiumLogin 1.3 By NotReference Enabled.");
		
	}
	
	public void onDisable() {
		//AuthUtils.clearLoggedIn();
		getLogger().info("INFO - PremiumLogin 1.3 By NotReference Disabled.. Byeee");
	}
	
	protected void setInstance(PremiumLoginMain diocane) {
		in = diocane;
	}
	
	public void logConsole(String x) {
		getLogger().info(x);
	}
	
	public static PremiumLoginMain i() {
		return in;
	}
	
	public boolean authmebungee() {
		return xb;
	}
	
	public Configuration getPlayersConf() {
		return players;
	}
	
	public void reloadPlayerConf() {
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info("ERR - Cannot reload players configuration..");
		}
	}
	
	public void sPlayerConf() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(players, new File(getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info("ERR - Cannot save players configuration..");
		}
	}
	
	public Configuration getConfig() {
		return configuration;
	}
	
	public void reloadConfig() {
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			} catch(Exception exc) {
				exc.printStackTrace();
				getLogger().info("ERR - Cannot reload configuration..");
			}

	}
	
	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().info("ERR - Cannot save configuration..");
		}
	}
	
}
