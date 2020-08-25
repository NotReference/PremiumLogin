package it.notreference.bungee.premiumlogin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.notreference.bungee.premiumlogin.api.PremiumLoginUpdate;
import it.notreference.bungee.premiumlogin.api.PremiumOnlineBuilder;
import it.notreference.bungee.premiumlogin.api.PremiumOnlineConnection;
import it.notreference.bungee.premiumlogin.commands.*;
import it.notreference.bungee.premiumlogin.listeners.Eventi;
//import it.notreference.bungee.premiumlogin.utils.AuthUtils;


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
 * @destination BungeeCord
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
	private Map<Class<? extends PlayerDataHandler>, PlayerDataHandler> handlers;

	private List<PremiumOnlineConnection> premiumConnections;

	/*

	Deprecated

	 */
	@Deprecated
	public PlayerAPI makeLockLoginAPI(ProxiedPlayer name) {
		return new PlayerAPI(name);
	}

	/**
	 *
	 * Copies file data to another reading all data to end.
	 *
	 * @param input the file to be copied
	 * @param output the output file
	 * @return operation status
	 */
	public boolean copyFileBytesContent(File input, File output) {

		try{


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

		}catch(IOException errore){
			errore.printStackTrace();
			return false;
		}

	}

	/**
	 *
	 * Generates a random string
	 *
	 * @param byteLength lunghezza array
	 * @return the random string
	 */
	public String randomString(int byteLength) {
     byte[] randomArray = new byte[byteLength];
     new Random().nextBytes(randomArray);
     String g = new String(randomArray, Charset.forName("UTF-8"));
     return base64_encode(g);
	}

	/**
	 *
	 * Parses the passed version to a ready readable api (V2) version.
	 *
	 * @param version the current version
	 * @return the parsed api version
	 * @deprecated does not work
	 */
	@Deprecated
	public String parseAPIVersion(String version) {

		int punti = version.length() - version.replaceAll(".","").length();
        if(punti == 3) {

        	//1.4.X (primi 3 = 1.4 // mancano 2 & length = 5)
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
	 * Decodes a string in base64
	 *
	 * @param str the string
	 * @return base64decoded string.
	 */
	public String base64_decode(String str) {
		byte[] bytes = Base64.getDecoder().decode(str);
		String decoded = new String(bytes);
		return decoded;
	}

	/**
	 *
	 * Checks updates.
	 *
	 * @return update packet
	 */
    public PremiumLoginUpdate checkForUpdates() throws IOException {
    	try {

    		/*

    		New api (v4)

    		 */

			URL url = new URL("https://blackdevelopers.eu/api/v4/premiumlogin/?checkUpdate=" + apiVersion);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
		//	String credenzialiAPI = base64_encode(API_AUTH_USER + ":" + API_AUTH_PSW);
		//	String authAPI = "Basic " + credenzialiAPI;
		//	con.setRequestProperty("Authorization", authAPI);
			con.setConnectTimeout(3000);
			con.setReadTimeout(3000);

			/*

			Disabling redirects

			 */
			con.setInstanceFollowRedirects(false);

			int status = con.getResponseCode();
			if(status == HttpURLConnection.HTTP_UNAUTHORIZED || status == HttpURLConnection.HTTP_FORBIDDEN) {
				if(status == HttpURLConnection.HTTP_FORBIDDEN) {
					throw new Exception("403");
				}
				if(status == HttpURLConnection.HTTP_UNAUTHORIZED) {
					throw new Exception("401");
				}
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			String response = content.toString();
			con.disconnect();
			if(response.contains("-5ERR")) {

				return new PremiumLoginUpdate(true, "-5_DEPRECATED");

			}

			if(response.contains("-2ERR")) {

				return new PremiumLoginUpdate(true, "-2_INVAILD");

			}

			if(response.contains("-4ERR")) {

				JsonObject apiResponse = new Gson().fromJson(response, JsonObject.class);
				String result = apiResponse.get("version").getAsString();
                return new PremiumLoginUpdate(true, result);
			}

			if(response.contains("success")) {
				return new PremiumLoginUpdate(false, ver);
			}

			return null;

		} catch(Exception exc) {
    		if(exc.toString() != null) {
    			if(exc.toString().contains("401")) {
    				logConsole("[API-Request] -> 401 -> Auth_Basic() [error: unauthorized [[401]]] Failed to authenticate. The authorization key is invaild or an error has occurred. Please retry later. (Maybe this plugin version is outdated?)");
    				return null;
    			} else {
    				logConsole("[API-Request] -> -1 -> An unexpected error while contacting our server has occurred. Please retry later.");
    				return null;
				}
			} else {
    			logConsole("[API-Request] -> -2 -> Unable to contact our servers. Please retry later.");
    			return null;
			}
		}
	}

	/**
	 *
	 * Starts the fetch data timer.
	 * Every 1 minute, an update will be async fetched.
	 *
	 */
	protected boolean scheduleUpdater() {

		try {


			getProxy().getScheduler().schedule(this, () -> {


				getLogger().log(Level.INFO, "§ePremiumLogin §8» §2Checking for updates..");

				try {

					PremiumLoginUpdate updatePacket = checkForUpdates();
					if(updatePacket == null) {
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cAn error has occurred.");
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cUnable to check for updates.");
					}
					if(updatePacket.getVersion().contains("-5_D")) {
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cThis plugin version is deprecated.");
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cDisabling..");
						getProxy().broadcast(new TextComponent("§7PremiumLogin is going to disable because this version is too outdated. Please update it now on SpigotMC."));
						getProxy().getScheduler().cancel(this);
						getProxy().getPluginManager().unregisterCommands(this);
						getProxy().getPluginManager().unregisterListeners(this);
						return;
					}
					if(updatePacket.getVersion().startsWith("-")) {
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cAn error has occurred. (& exception throw)");
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cUnable to check for updates.");
					}
					if(updatePacket.isAvaliable()) {

						getLogger().log(Level.WARNING, "§ePremiumLogin §8» §cYou are running an older version of PremiumLogin ( " + ver + " )");
						getLogger().log(Level.WARNING, "§ePremiumLogin §8»  §7Download the newest version ( " + updatePacket.getVersion() + " ) from : " + SPIGOT_MC);
					} else {
						getLogger().log(Level.INFO, "§ePremiumLogin §8» §2You are running the latest version of PremiumLogin! ^^ ( " + ver + " )");
					    return;
					}
				} catch(Exception exc) {
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cAn error has occurred. (& exception throw)");
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cUnable to check for updates.");
				}


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


	/**
	 *
	 * contains but IgnoringCases
	 *
	 * @param content
	 * @param target
	 * @return true if contains, false if not
	 */
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
	public void setupAutoLanguage() {

		/*

lock-login: '&aSuccessfully logged in with LockLogin API Support.'
join-with-premium: '&cUnable to connect. Your session is not vaild.'
auto-login-premium: '&aPremium account found. You are now logged in.'
error-generic: '&cAn error has occurred.'
unable: '&cUnable to login.'
unable-lobby: '&cUnable to send you to the lobby server. (Offline?, Not exists?)'
default-login-system-switch-to-premiumlogin: '&aYour default login system has been switched to PremiumLogin.'
default-login-system-switch-to-authme: '&aYou disabled autologin.'
enabled-autologin: '&aYou can now autologin with premium account.'
disable-autologin: '&aYou disabled autologin with premium account.'
no-premium: '&cYou are not premium.'
no-legacy: '&cUnable to join. Legacy is not allowed.'
not-used-premium-launcher: '&cFor enable this function, you need to be logged from the official &cMinecraft Premium Launcher.'
no-perms: '&cYou do not have permissions.'
staff-disable: '&cAn administrator disabled PremiumLogin for you. Please rejoin.'

		 */


		/*

		1.7.1: Bye bye autolanguage.

		 */
		return;


	//	if(ConfigUtils.getConfStr("auto-language") == null || !ConfigUtils.getConfBool("auto-language")) {
		//	return;
		//}

		/*
		if(ConfigUtils.getConfBool("auto-language")) {
			String lcode = ConfigUtils.getConfStr("language");
			if(lcode.equalsIgnoreCase("it")) {

				set("join-with-premium", "&cLa tua sessione non .e1. valida. Entra con il tuo account Premium.");
				set("auto-login-premium", "&aAccount Premium Rilevato. Autenticazione automatica.");
				set("error-generic", "&cSi .e1. verificato un errore.");
				set("unable", "&cE'' stato impossibile effettuare l''autenticazione automatica.");
				set("unable-lobby", "&cE'' stato impossibile inviarti al server principale. &c(Offline? Non esiste?)");
				set("default-login-system-switch-to-premiumlogin", "&aOra puoi autologgarti.");
				set("default-login-system-switch-to-authme", "&aHai disabilitato l''autologin.");
				set("no-premium", "&cDevi essere un giocatore Premium.");
				set("no-legacy", "&cSpiacenti, questa versione ha un sistema di autenticazione &ctroppo vecchio, utilizza almeno la 1.6+!");
				set("enabled-autologin", "&aOra puoi effettuare l''autenticazione automatica.");
				set("disable-autologin", "&aHai disabilitato l''autenticazione automatica.");
				set("not-used-premium-launcher", "&cDevi essere un giocatore Premium &cloggato col launcher ufficiale.");
				set( "no-perms" ,  "&cNon hai il permesso." );
				set("staff-disable", "&cUn amministratore ti ha disabilitato l''autenticazione &cautomatica. Rientra.");
				saveConfig();
				reloadTempConfig();
				logConsole("La lingua è stata impostata ad Italiano come scelto dal config!");
				return;
			}
			if(lcode.equalsIgnoreCase("en")) {

				set("join-with-premium", "&cUnable to connect. Your session is not vaild.");
				set("auto-login-premium", "&aPremium account found. You are now logged in.");
				set("error-generic", "&cAn error has occurred.");
				set("unable", "&cUnable to login.");
				set("unable-lobby", "&cUnable to send you to the lobby server. (Offline?, Not exists?)");
				set("default-login-system-switch-to-premiumlogin", "&aYour default login system has been switched to PremiumLogin.");
				set("default-login-system-switch-to-authme", "&aYou disabled autologin.");
				set("no-premium", "&cYou are not premium.");
				set("no-legacy", "&cUnable to join. Legacy is not allowed.");
				set("enabled-autologin", "&aYou can now autologin with premium account.");
				set("disable-autologin", "&aYou disabled autologin with premium account.");
				set("not-used-premium-launcher", "&cFor enable this function, you need to be logged from the official &cMinecraft Premium Launcher.");
				set( "no-perms" ,  "&cYou do not have permissions." );
				set("staff-disable", "&cAn administrator disabled PremiumLogin for you. Please rejoin.");
				saveConfig();
				reloadTempConfig();
				logConsole("Language has been set to english.");
				return;
			}
			if(lcode.equalsIgnoreCase("es")) {

				set("join-with-premium", "&cSu sesión parece no ser válida. Inicia sesión con su cuenta premium.");
				set("auto-login-premium", "&aCuenta premium detectada. Autenticación automática");
				set("error-generic", "&cHa ocurrido un error.");
				set("unable", "&cHa ocurrido un error mientras te estabas autenticando.");
				set("unable-lobby", "&cHa ocurrido un error mientras te estabas conectando al Lobby.");
				set("default-login-system-switch-to-premiumlogin", "&aAhora puedes efectuar autenticacion automatica.");
				set("default-login-system-switch-to-authme", "&aHas desactivado el login automatico.");
				set("no-premium", "&cDebes ser un jugador Premium.");
				set("no-legacy", "&cLo sentimos, esta versión tiene un sistema de autenticación deprecado, usa almenos la vercion 1.6+.");
				set("enabled-autologin", "&aAhora puedes efectuar autenticacion automatica.");
				set("disable-autologin", "&aHas desactivado el login automatico.");
				set("not-used-premium-launcher", "&cDebes ser un jugador Premium &cautenticado con launcher oficial.");
				set( "no-perms" ,  "&cNo tienes permisos." );
				set("staff-disable", "&cUn administrador te ha desactivado la autenticación automatica. Re-ingresa.");
				saveConfig();
				reloadTempConfig();
				logConsole("¡El idioma se ha configurado en español segúen lo elegido por la configuración!");
				return;
			}

			logConsole("Invaild language code: " + lcode + ". Using default config messages..");
		}
        */


	}

	/**
	 *
	 *  Builds a new PremiumOnlineConnection.
	 *
	 */
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

	/**
	 * Returns if a player is connected.
	 *
	 *
	 * @param playerName
	 *
	 */
	public boolean isPremiumConnected(String playerName) {

		for(PremiumOnlineConnection con: premiumConnections) {
   if(con.getPlayerName() == playerName) {
   	return true;
   }
		}
       return false;
	}

	/**
	 *
	 * Removes a connection.
	 *
	 * @param connection
	 */
	public void removeConnection(PremiumOnlineConnection connection) {
		if(!premiumConnections.contains(connection)) {
			return;
		}
		premiumConnections.remove(connection);
	}

	protected String randomSID(String playerName, String playerUUID, String playerIP, String loginPlugin) {
		byte[] bytes = new byte[15];
		Random random = new Random();
		random.nextBytes(bytes);
		byte[] random2 = new byte[10];
		random.nextBytes(random2);
		String encodedBytes = base64_encode(new String(bytes));
		String loginPl = base64_encode(loginPlugin.toLowerCase());
		String sid = loginPl + encodedBytes + base64_encode(playerUUID) + base64_encode("premiumLogin17") + base64_encode(new String(random2)) + base64_encode(playerIP) + base64_encode(playerName);
		return sid;
	}

	/**
	 *
	 * Returns a new data handler
	 *
	 * @param classe
	 * @param <T>
	 * @return
	 */
	public <T> T newDataHandler(Class<?> classe) {

		return (T) handlers.get(classe);

	}


	/**
	 *
	 * Converts a string to an input stream.
	 *
	 * @param e the string
	 * @return
	 */
	public InputStream stringToInputStream(String e) {
		return new ByteArrayInputStream(e.getBytes());
	}

	@Override
	public void onEnable() {

		configManager = new PremiumLoginFilesUtils(this);
		if(configManager.deleteAllTempFiles()) {
			getLogger().info("INFO - All temp files have been deleted.");
		}
		handlers = new HashMap<>();
		/*

		Dir setup...

		 */
		 if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "players.yml");
        File file3 = new File(getDataFolder(), "codes.yml");
        File file4 = new File(getDataFolder(), "playerdata.yml");

   /*

   Config setup..

    */

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
                in.close();
            } catch (IOException e) {
            	e.printStackTrace();
           	    getLogger().severe("ERR - Unable to create the configuration; if you are using Linux, try to start the server using sudo.");
            }
        }

        /*


        Config Fix (1.6.3)

         Unnecessary.

         */
        /*
        getLogger().info("INFO - [1.6.3 Fix] Fixing the configuration file.. Please wait.. This can take few seconds..");
        try {

        File tempFile = new File(getDataFolder() + convertInputStreamToFile(getResourceAsStream("config.yml")));
        if(copyFileBytesContent(tempFile, file)) {

        	getLogger().info("SUCCESS - Configuration successfully fixed.");

		} else {

        	getLogger().severe("ERROR - Exception log for config-/fix-1.6.3: AT it.notreference.bungee.premiumlogin.PremiumLoginMain.copyFileBytesContent(tempFile, config). Please proivde this string to me on spigotmc.");
			getLogger().severe("ERROR - Failed to fix the configuration. Please retry restarting the server and deleting the file.");
			return;

		}

        if(!tempFile.delete()) {
        	getLogger().warning("WARNING - Unable to delete the temp file. Please delete it manually.");
		}

		} catch(Exception exc) {
        	exc.printStackTrace();
            getLogger().severe("ERROR - Failed to fix the configuration. Please retry restarting the server and deleting the file.");
            return;
		}
        */


        if (!file2.exists()) {
            try (InputStream in = getResourceAsStream("players.yml")) {
                Files.copy(in, file2.toPath());
                in.close();
            } catch (IOException e) {
				e.printStackTrace();
           	    getLogger().severe("ERR - Unable to create the players file; if you are using Linux, try to start the server using sudo.");

            }
        }


		if (!file4.exists()) {
			//try (InputStream in = getResourceAsStream("playerdata.yml")) {
			//	Files.copy(in, file2.toPath());
				//in.close();
			//} catch (IOException e) {
				//e.printStackTrace();
				//getLogger().severe("ERR - Unable to create the player data file; if you are using Linux, try to start the server using sudo.");

			//}
		}

		if (!file3.exists()) {
			try (InputStream in = getResourceAsStream("codes.yml")) {
				Files.copy(in, file3.toPath());
				in.close();
			} catch (IOException e) {
			}
		}
		
		/*

		Loading config...

		 */
		try {
			File configNew = configManager.copyConfiguration();
		configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configNew);
		tempFile = configNew;
		currentConfigPath = configNew.getAbsolutePath();
				// new File(getDataFolder(), "config.yml"));
		} catch(Exception exc) {
			exc.printStackTrace();
			getLogger().severe("ERR - Unable to load the configuration.. Try to delete it.. Disabling.. If you are using Linux, try to start the server using sudo.");
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
			getLogger().warning("WARNING - For use PremiumLogin you must turn off the online-mode.");
			return;
		}

	
		if(getProxy().getPluginManager().getPlugin("PremiumLock") != null) {
			getLogger().warning("WARNING - You are using PremiumLock: This may be cause several issues.");
		return;
                }

		
		try {
		reloadTempConfig();
		saveConfig();
		reloadTempConfig();
		} catch(Exception exc) {
			logConsole("ERR - Unable to initialize configuration.. Some operation can be bugged.");
		}


		/*

		1.6.3: Updater.

		 */
		getLogger().log(Level.INFO, "§ePremiumLogin §8» §2Checking for updates..");
			try {

				PremiumLoginUpdate updatePacket = checkForUpdates();
				if (updatePacket == null) {
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cAn error has occurred.");
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cUnable to check for updates.");
					if (ConfigUtils.getConfBool("ignore-updater-error")) {
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §eError ignored as set in configuration.");
					} else {
						return;
					}
				}
				if (updatePacket.getVersion().contains("-5_D")) {
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cThis plugin version is deprecated.");
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cDisabling..");
					getLogger().log(Level.WARNING, "§ePremiumLogin §8»  §7Download the newest version ( ? ) from : " + SPIGOT_MC);
					if (ConfigUtils.getConfBool("ignore-updater-error")) {
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §eThis error can't be ignored.");
					}
					getProxy().broadcast(new TextComponent("§7PremiumLogin is going to disable because this version is too outdated. Please update it now on SpigotMC."));
					getProxy().getScheduler().cancel(this);
					getProxy().getPluginManager().unregisterCommands(this);
					getProxy().getPluginManager().unregisterListeners(this);
					return;
				}
				if (updatePacket.getVersion().startsWith("-")) {
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cAn error has occurred. (& exception throw)");
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cUnable to check for updates.");
					if (ConfigUtils.getConfBool("ignore-updater-error")) {
						getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §eError ignored as set in configuration.");
					}
				}

				if (updatePacket.isAvaliable()) {
					getLogger().log(Level.WARNING, "§ePremiumLogin §8» §cYou are running an older version of PremiumLogin ( " + ver + " )");
					getLogger().log(Level.WARNING, "§ePremiumLogin §8»  §7Download the newest version ( " + updatePacket.getVersion() + " ) from : " + SPIGOT_MC);
				} else {
					getLogger().log(Level.INFO, "§ePremiumLogin §8» §2You are running the latest version of PremiumLogin! ^^ ( " + ver + " )");
				}

			} catch (Exception exc) {
				exc.printStackTrace();
				getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cAn error has occurred. (& exception throw)");
				getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §cUnable to check for updates.");
				if (ConfigUtils.getConfBool("ignore-updater-error")) {
					getLogger().log(Level.SEVERE, "§ePremiumLogin §8» §eError ignored as set in configuration.");
				}
			}

      if(scheduleUpdater()) {

      	getLogger().info("ERROR - Update fetch task successfully scheduled.");

	  } else {
      	getLogger().severe("ERROR - Unable to schedule the updater!");
	  }

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

		/*

		BungeeCord Channel Compatible with all versions.

		 */

		getProxy().registerChannel("BungeeCord");

		//handlers.put(PlayerDataManager.class, new PlayerDataManager(this, new DataLoader(configManager, new File(getDataFolder(), "playerdata.yml"))));

		//LockLogin API Support (1.4)
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

		/*

		^^

		 */
		        /*

        1.6.3: Autolanguage

         */
		setupAutoLanguage();

		getLogger().info("SUCCESS - PremiumLogin " + ver + " By NotReference Enabled.");
		if(!setupconfigfix) {

			/*

			Da rimuovere nella  1.7.3

			 */

			getLogger().info("NOTE - PremiumLogin 1.7.2 includes configuration fix ,some improvements and new features, if you don't deleted configuration, please delete now and restart.");
		}
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


		if(deleteTempConfig()) {

			getLogger().info("INFO - The temp config has been deleted!");

		}
		if(configManager.deleteAllTempFiles()) {
			getLogger().info("INFO - All temp files have been deleted.");
		}
		getLogger().info("INFO - PremiumLogin " + ver + " By NotReference Disabled. Goodbye.");
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
	 * Deletes the temp config.
	 *
	 * @return
	 */
	public boolean deleteTempConfig() {
     return tempFile.delete();
	}

	/**
	 *
	 * Sets the temp file.
	 *
	 */
	public void setTempFile(File f) {

		tempFile = f;

	}

	/**
	 *
	 * Get the temp config.
	 *
	 * @return temp config.
	 */
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
			setupAutoLanguage();
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
	 * @deprecated temp save because the file is temp
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
