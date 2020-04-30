package it.notreference.bungee.premiumlogin.utils.authentication;

import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.events.PremiumAutologinEvent;
import it.notreference.bungee.premiumlogin.utils.*;
import ml.karmaconfigs.LockLogin.BungeeCord.API.PlayerAPI;
import ml.karmaconfigs.LockLogin.BungeeCord.LockLoginBungee;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.net.InetSocketAddress;
import java.util.UUID;



/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class AuthenticationHandler {


	protected static void error(String msg) {
		PremiumLoginMain.i().getLogger().severe("( AuthenticationHandler ) - ERROR - " + msg);
	}

	protected static void info(String infoMsg) {
		PremiumLoginMain.i().getLogger().info("( AuthenticationHandler ) - INFO - " + infoMsg);
	}

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
			error("Unable to perform premium login to: " + p.getName() + ", this player isn't premium.");
			return 2;
		}

		if (key.getConType() == ConnectionType.LEGACY) {

			if (!ConfigUtils.allowLegacy()) {
			    error("Legacy not allowed. I have blocked PremiumLogin for " + p.getName());
				return 7;
			}
			if (!UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
				error("No Online Mode: " + p.getName() + ". Login not performed.");
				return 4;
			}
		} else {

			if (!UUIDUtils.isPremiumConnection(p)) {
				error("No Online Mode: " + p.getName() + ". Login not performed.");
				return 4;
			}
		}

		if (!p.isConnected()) {
			return 3;
		}

		/*

		1.4: Checking LockLogin Hook.
		1.7.1: New API.

		 */
	    info("Checking LockLogin Status..");
	    info("Note: Using LockLogin 3.0.2 New API!");
		if (PremiumLoginMain.i().isHooked("LockLogin")) {
			LockLoginBungee lockLogin = (LockLoginBungee) PremiumLoginMain.i().getProxy().getPluginManager().getPlugin("LockLogin");
			info("Hooked into LockLogin!");
		    info("Performing auto login to: " + p.getName());
			try {
				PlayerAPI api = new PlayerAPI(p);
				api.setLogged(true);
				PluginUtils.sendParseColors(p, ConfigUtils.getConfStr("auto-login-premium"));
			   info("Successfully logged in: user = " + p.getName());
				PluginUtils.logConsole(p.getName() + " has been forcelogged (premium mode).");
				PluginUtils.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
				PremiumLoginEventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
				PremiumLoginMain.i().handleConnectionAsync(p.getName(), UUIDUtils.isPremiumConnectionLegacy(p), p.getUniqueId().toString(), p.getAddress().getHostString(), true, LoginPlugin.LOCKLOGIN);
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


		info("Performing auto login to: " + p.getName());
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
			info("Successfully logged in: user = " + p.getName());
			if (key.getAuthType() == AuthType.AUTO) {
				PluginUtils.logConsole(p.getName() + " has been forcelogged (premium mode).");
				PluginUtils.sendParseColors(p, PremiumLoginMain.i().getConfig().getString("auto-login-premium"));
				PluginUtils.logStaff(ConfigUtils.getConfStr("user-forcelogged"), new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostName()));
				PremiumLoginEventManager.fire(new PremiumAutologinEvent(p, p.getName(), p.getPendingConnection(), p.getUniqueId(), key));
				PremiumLoginMain.i().handleConnectionAsync(p.getName(), UUIDUtils.isPremiumConnectionLegacy(p), p.getUniqueId().toString(), p.getAddress().getHostString(), true, LoginPlugin.AUTHME);
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
