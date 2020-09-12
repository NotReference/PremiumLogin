package it.notreference.bungee.premiumlogin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.notreference.bungee.premiumlogin.api.PremiumLoginUpdate;
import it.notreference.bungee.premiumlogin.api.PremiumOnlineBuilder;
import it.notreference.bungee.premiumlogin.api.PremiumOnlineConnection;
import it.notreference.bungee.premiumlogin.commands.*;
import it.notreference.bungee.premiumlogin.listeners.Eventi;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.LoginPlugin;
import it.notreference.bungee.premiumlogin.utils.data.PlayerDataHandler;
import it.notreference.bungee.premiumlogin.utils.data.PlayerDataManager;
import it.notreference.bungee.premiumlogin.utils.files.DataLoader;
import it.notreference.bungee.premiumlogin.utils.files.PremiumLoginFilesUtils;
import ml.karmaconfigs.LockLogin.BungeeCord.API.PlayerAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 *
 */

public class PremiumLoginMain extends Plugin {

	private static PremiumLoginMain in;
	private Configuration configuration;
	private Configuration players;
	private boolean locklogin = false;
	private boolean setupconfigfix = false;
	private File tempFile;
	private String ver = "1.7.2";
	private String apiVersion = "17-2";
	private PremiumLoginFilesUtils configManager;
	private boolean ab = false;
	private String currentConfigPath = "";
	private String SPIGOT_MC = "https://www.spigotmc.org/resources/premiumlogin.76336/";
	private boolean luckperms = false;

	private List<PremiumOnlineConnection> premiumConnections;

	@Deprecated
	public PlayerAPI makeLockLoginAPI(ProxiedPlayer name) {
		return new PlayerAPI(name);
	}

	public boolean copyFileBytesContent(File input, File output) {

		try {

			FileInputStream ins = new FileInputStream(input);
			FileOutputStream outs = new FileOutputStream(output);

			byte[] buffer = new byte[1024];

			int length;


			while ((length = ins.read(buffer)) > 0){
				outs.write(buffer, 0, length);
			}

			ins.close();
			outs.close();

			return true;

		} catch(IOException errore) {
		     // errore.printStackTrace();
			return false;
		}

	}


  public String randomString(int byteLength) {
      byte[] randomArray = new byte[byteLength];
      new Random().nextBytes(randomArray);
      String g = new String(randomArray, Charset.forName("UTF-8"));
      return base64_encode(g);
  }

	@Deprecated
	public String parseAPIVersion(String version) {

		int punti = version.length() - version.replaceAll(".","").length();

        if(punti == 3) {
			String versioneSenzaUltimoNumero = version.substring(0, 3);
			String ultimi2  = version.substring(Math.max(version.length() - 2, 0));

			String versione = versioneSenzaUltimoNumero.replace(".", "") + "-" + ultimi2.replace(".", "");
			return versione;

		} else if(punti == 2) {

        	return version.replace(".", "");

		}
                return "INVAILD";
	}

	/**
	 *
	 * Encodes a string in base64.
	 *
	 * @param str the string
	 * @return base64encoded string
	 */
	public String base64_encode(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}

	/**
	 *
	 * Decodes a base64 string 
	 *
	 * @param str the string
	 * @return decoded string.
	 */
	public String base64_decode(String str) {
		byte[] bytes = Base64.getDecoder().decode(str);
		String decoded = new String(bytes);
		return decoded;
	}

	
    @Deprecated
    public PremiumLoginUpdate checkForUpdates() throws IOException {
			
        return new PremiumLoginUpdate(false, ver);
		
    }

	@Deprecated
	protected boolean scheduleUpdater() {

		try {


			getProxy().getScheduler().schedule(this, () -> {


				getLogger().log(Level.INFO, "§ePremiumLogin §8» §2Checking for updates..");

				getLogger().log(Level.INFO, "§ePremiumLogin §8» §7Updates are no longer supported.");


			}, 1,420, TimeUnit.SECONDS);

			return true;
		} catch(Exception exc) {
			return false;
		}
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

	public boolean containsIgnoreCase(String target, String content) {
		content = content.toLowerCase();
		target = target.toLowerCase();
		if(target.contains(content)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * Alias of ConfigUtils.set(..);
	 *
	 * @param p
	 * @param v
	 */
	protected void set(String p, String v) {ConfigUtils.set(p,v);}


	/**
	 *
	 * Auto setup language if set in configuration.
	 *
	 */
	@Deprecated
	public void setupAutoLanguage() {
         return;
	}

	public void handleConnectionAsync(String playerName, boolean isLegacy, String playerUUID, String playerAddress, boolean isOnline, LoginPlugin loginPlugin) {

		getProxy().getScheduler().runAsync(this, () -> {
			if (!isOnline) {
				return;
			}

			PremiumOnlineConnection conPacket = new PremiumOnlineBuilder()
					.setLegacy(isLegacy)
					.setSession(randomSID(playerName, playerUUID, playerAddress, loginPlugin.toString()))
					.setUser(playerName)
					.setUUID(playerUUID)
					.buildConnection();

			for (PremiumOnlineConnection con : premiumConnections) {
				if (!conPacket.compare(con)) {
					return;
				}
			}

			premiumConnections.add(conPacket);


		});
	}

	/**
	 *
	 * Returns all player connections.
	 *
	 * @return
	 */
	public List<PremiumOnlineConnection> getPremiumConnections() {
		return new ArrayList<PremiumOnlineConnection>(premiumConnections);
	}

	public boolean isPremiumConnected(String playerName) {

	 for(PremiumOnlineConnection con: premiumConnections) {
          if(con.getPlayerName() == playerName) {
   	    return true;
           }
	 }
       return false;
       }

	public void removeConnection(PremiumOnlineConnection connection) {
		if(!premiumConnections.contains(connection)) {
			return;
		}
		premiumConnections.remove(connection);
	}

	@Deprecated
	protected String randomSID(String playerName, String playerUUID, String playerIP, String loginPlugin) {
		byte[] bytes = new byte[15];
		Random random = new Random();
		random.nextBytes(bytes);
		byte[] random2 = new byte[10];
		random.nextBytes(random2);
		String encodedBytes = base64_encode(new String(bytes));
		String loginPl = base64_encode(loginPlugin.toLowerCase());
		String sid = loginPl + encodedBytes + base64_encode(playerUUID) + base64_encode("premiumLogin172") + base64_encode(new String(random2)) + base64_encode(playerIP) + base64_encode(playerName);
		return sid;
	}

	public InputStream stringToInputStream(String e) {
		return new ByteArrayInputStream(e.getBytes());
	}

	@Override
	public void onEnable() {

	configManager = new PremiumLoginFilesUtils(this);
	if(configManager.deleteAllTempFiles())
	   getLogger().info("INFO - All temp files have been deleted.");
	 if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "players.yml");
        File file3 = new File(getDataFolder(), "codes.yml");
        File file4 = new File(getDataFolder(), "playerdata.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
                in.close();
            } catch (IOException e) {
            	e.printStackTrace();
           	    getLogger().severe("ERROR - Unable to create the configuration.");
            }
        }

        if (!file2.exists()) {
            try (InputStream in = getResourceAsStream("players.yml")) {
                Files.copy(in, file2.toPath());
                in.close();
            } catch (IOException e) {
		    e.printStackTrace();
           	    getLogger().severe("ERR - Unable to create the players file; if you are using Linux, try to start the server using sudo.");

            }
        }


		if (!file3.exists()) {
			try (InputStream in = getResourceAsStream("codes.yml")) {
				Files.copy(in, file3.toPath());
				in.close();
			} catch (IOException e) {
			}
		}
		
		try {
		File configNew = configManager.copyConfiguration();
		configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configNew);
		tempFile = configNew;
		currentConfigPath = configNew.getAbsolutePath();
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().severe("ERR - Unable to load the configuration.. Disabling..");
			return;
		}
		
		try {
			players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "players.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().severe("ERR - Unable to load players data.. Disabling..");
			return;
		}
		
		if(getProxy().getConfig().isOnlineMode()) {
			getLogger().warning("WARNING - Online-mode is not supported.");
			return;
		}

	
		if(getProxy().getPluginManager().getPlugin("PremiumLock") != null) {
		getLogger().warning("WARNING - PremiumLock is no longer supported!");
		return;
                }

		
		try {
		reloadTempConfig();
		saveConfig();
		reloadTempConfig();
		} catch(Exception exc) {
			logConsole("ERR - Unable to initialize configuration.. Some operation can be bugged.");
		}


		

      if(scheduleUpdater())
      getLogger().info("SUCCESS - Update fetch task successfully scheduled.");

       premiumConnections = new ArrayList<PremiumOnlineConnection>();

		try {
			getProxy().getPluginManager().registerListener(this, new Eventi());
			getProxy().getPluginManager().registerCommand(this, new PremiumCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumLoginCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumLookUpCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumReloadCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumAddCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumRemoveCmd());
			getProxy().getPluginManager().registerCommand(this, new PremiumListCmd());
		} catch(Exception exc) {
			getLogger().severe("ERROR - Unable to enable the plugin. Please retry. Disabling...");
			return;
		}
		setInstance(this);
		getProxy().registerChannel("BungeeCord");
		if(getProxy().getPluginManager().getPlugin("LockLogin") != null) {
			locklogin = true;
			getLogger().info("HOOK - LockLogin Found..");
			getLogger().info("HOOK - Hooked into LockLogin successfully!");
		}

		if(getProxy().getPluginManager().getPlugin("AuthMeBungee") != null) {
			ab = true;
			getLogger().info("HOOK - AuthMeBungee Found..");
			getLogger().info("HOOK - Hooked into AuthMeBungee successfully!");
		}
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
		if(pluginName.equalsIgnoreCase("authmebungee")) {
			return ab;
		}
		return false;		
	}
	public void onDisable() {
		if(deleteTempConfig())
		getLogger().info("INFO - The temp config has been deleted!");
		if(configManager.deleteAllTempFiles())
	        getLogger().info("INFO - All temp files have been deleted.");
		
		getLogger().info("INFO - PremiumLogin " + ver + " By NotReference Disabled. Goodbye.");
	}
	
	protected void setInstance(PremiumLoginMain main) {
		in = main;
	}

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
	 * Deletes the temp config.
	 *
	 * @return
	 */
	public boolean deleteTempConfig() {
         return tempFile.delete();
	}
	
	public void setTempFile(File f) {

		tempFile = f;

	}

	public File getTempConfig() {
		return tempFile;
	}

	/**
	 *
	 * Reloads the configuration.
	 *
	 */
	public void reloadConfig() {

		deleteTempConfig();

		try {
			File f = configManager.copyConfiguration();
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			currentConfigPath = f.getAbsolutePath();
			setTempFile(f);
		} catch (Exception exc) {
			exc.printStackTrace();
			getLogger().severe("ERR - Unable to reload configuration..");
		}
	}

		/**
         *
         * Reloads the configuration. (temp)
         *
         */
	public void reloadTempConfig() {
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(tempFile);
		} catch(Exception exc) {
				exc.printStackTrace();
				getLogger().severe("ERR - Unable to reload configuration..");
		}

	}

	/**
	 *
	 * Saves the configuration.
	 *
	 * @deprecated 
	 */
	@Deprecated
	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, tempFile);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("ERR - Unable to save configuration..");
		}
	}
	
}
