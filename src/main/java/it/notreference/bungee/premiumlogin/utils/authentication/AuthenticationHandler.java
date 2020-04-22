package it.notreference.bungee.premiumlogin.utils.authentication;

import io.github.karmaconfigs.Bungee.API.PlayerAPI;
import io.github.karmaconfigs.Bungee.LockLoginBungee;
import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.events.PremiumAutologinEvent;
import it.notreference.bungee.premiumlogin.utils.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;



/**
 *
 * PremiumLogin 1.6.5 By NotReference
 *
 * @author NotReference
 * @version 1.6.5
 * @destination BungeeCord
 *
 */

public class AuthenticationHandler {

	/**
	 *
	 * Performs premium auto login.
	 *
	 * @param p the player
	 * @param key the key
	 * @return login response in int.
	 */
	public static int login(ProxiedPlayer p, AuthenticationKey key) {

		if (!ConfigUtils.hasPremiumAutoLogin(p)) {
			return 6;
		}

		if (!UUIDUtils.isPremium(p)) {
			PluginUtils.logConsole("[err] nopremium_user: " + p.getName());
			return 2;
		}

		if (key.getConType() == TipoConnessione.LEGACY) {

			if (!ConfigUtils.allowLegacy()) {
				PluginUtils.logConsole("[err=>[nolegacy]] connectionblock_user: " + p.getName());
				return 7;
			}
			if (!UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
				PluginUtils.logConsole("[err] no_launcher_user: " + p.getName());
				return 4;
			}
		} else {

			if (!UUIDUtils.isPremiumConnection(p)) {
				PluginUtils.logConsole("[err] no_launcher_user: " + p.getName());
				return 4;
			}
		}

		if (!p.isConnected()) {
			return 3;
		}

		/*

		1.4: Checking LockLogin Hook.

		 */
		PluginUtils.logConsole("[check] checking locklogin..");
		if (PremiumLoginMain.i().isHooked("LockLogin")) {
			LockLoginBungee lockLogin = (LockLoginBungee) PremiumLoginMain.i().getProxy().getPluginManager().getPlugin("LockLogin");
			PluginUtils.logConsole("[locklogin] hooked into locklogin.");
			PluginUtils.logConsole("[action] forcelogging (user= " + p.getName() + ")");
			try {
				String version = lockLogin.getDescription().getVersion().replaceAll("[aA-zZ]", "").replace(".", "");
				/*

				LockLogin 2.1.8 implemented new API.
				The minimum required version is 2.1.8 (218)
				(In 1.6.2 was 215)

				 */
				if (Integer.parseInt(version) >= 218) {
					PlayerAPI api = new PlayerAPI(p);
					api.setLogged(true, ConfigUtils.getConfStr("lock-login"));
				} else {
					PlayerAPI api = new PlayerAPI(lockLogin, p);
					api.setLogged(true, ConfigUtils.getConfStr("lock-login"));
					PluginUtils.logConsole("[PremiumLogin - LockLogin] WARNING: You are using an old version of LockLogin ( " + lockLogin.getDescription().getVersion() + " )");
					PluginUtils.logConsole("[PremiumLogin - LockLogin] Download the latest version from https://www.spigotmc.org/resources/gsa-locklogin.75156/");
				}
				PluginUtils.sendParseColors(p, ConfigUtils.getConfStr("auto-login-premium"));
				PluginUtils.logConsole("[[premium:forcelogin] donelogin_user: " + p.getName());
				PluginUtils.logConsole(p.getName() + " has been forcelogged (premium mode).");
				PluginUtils.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
				PremiumLoginEventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
				if(ConfigUtils.getConfBool("send-to-lobby")) {

					try {

                   ServerInfo infoHub = PremiumLoginMain.i().getProxy().getServerInfo(ConfigUtils.getConfStr("lobby-server"));
                   p.connect(infoHub);

					} catch(Exception exc) {

						PluginUtils.logConsole("Unable to send user " + p.getName() + " to the lobby server. (Invaild name? Offline?)");
                        PluginUtils.send(p, ConfigUtils.getConfStr("unable-lobby"));
					}

				}
				return 1;
			} catch (Exception exc) {
				PluginUtils.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("unable"));
				PluginUtils.logConsole("Unable to premium login (with locklogin) user " + p.getName());
				exc.printStackTrace();
				return 6;
			}
		}


		PluginUtils.logConsole("[action] forcelogging (user= " + p.getName() + ") using AuthMeAPI.");
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("AuthMe.v2");
			out.writeUTF("perform.login");
			out.writeUTF(p.getName());
			try {
				key.playerServer().sendData("BungeeCord", out.toByteArray());
				p.getServer().sendData("BungeeCord", out.toByteArray());
			} catch(Exception exc) {

				PluginUtils.logConsole("The player server is invaild.");


			}
			try {
				PremiumLoginMain.i().getProxy().getServerInfo(ConfigUtils.getConfStr("auth-server")).sendData("BungeeCord", out.toByteArray());
			} catch(Exception exc) {
				PluginUtils.logConsole("The specifed auth server is invaild.");
			}
			PluginUtils.logConsole("[[premium:forcelogin] donelogin_user: " + p.getName());
			if (key.getAuthType() == AuthType.AUTO) {
				PluginUtils.logConsole(p.getName() + " has been forcelogged (premium mode).");
				PluginUtils.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("auto-login-premium"));
				PluginUtils.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
				PremiumLoginEventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
				if(ConfigUtils.getConfBool("send-to-lobby")) {

					try {

						ServerInfo infoHub = PremiumLoginMain.i().getProxy().getServerInfo(ConfigUtils.getConfStr("lobby-server"));
						p.connect(infoHub);

					} catch(Exception exc) {

						PluginUtils.logConsole("Unable to send user " + p.getName() + " to the lobby server. (Invaild name? Offline?)");
						PluginUtils.send(p, ConfigUtils.getConfStr("unable-lobby"));
					}

				}
			}
			return 1;
		} catch (Exception exc) {
			PluginUtils.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("unable"));
			PluginUtils.logConsole("Unable to premium login " + p.getName());
			exc.printStackTrace();
			return 6;
		}
	}
}
