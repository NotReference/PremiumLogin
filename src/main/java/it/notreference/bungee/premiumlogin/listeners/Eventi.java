package it.notreference.bungee.premiumlogin.listeners;

//import java.lang.reflect.Field;
//import java.nio.charset.Charset;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.SetupMethod;
import it.notreference.bungee.premiumlogin.api.events.PremiumJoinEvent;
import it.notreference.bungee.premiumlogin.api.events.PremiumQuitEvent;
import it.notreference.bungee.premiumlogin.api.events.UUIDSetupEvent;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthType;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationHandler;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.PluginUtils;
import it.notreference.bungee.premiumlogin.utils.PlaceholderConf;
import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
//import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;


/**
 *
 * PremiumLogin 1.6.5 By NotReference
 *
 * @author NotReference
 * @version 1.6.5
 * @destination BungeeCord
 *
 */

public class Eventi implements Listener {


	@EventHandler(priority = EventPriority.HIGHEST)
	public void connectionsetup(PreLoginEvent event) {
		PendingConnection connection = event.getConnection();

		if (ConfigUtils.hasPremiumAutoLogin(connection.getName())) {
			try {
				connection.setOnlineMode(true);
				PremiumLoginMain.i().logConsole("Successfully set " + connection.getName() + "'s connection to Premium.");
			} catch(Exception exc) {
				connection.disconnect("§cUnable to join. Session server returned an invaild response. Please retry.");
				PremiumLoginMain.i().getLogger().severe("ERROR - Unable to set " + connection.getName() + " to Premium. (Maybe sessions server offline?)");
			}
		}
	}


	@EventHandler (priority = EventPriority.HIGH)
	public void entratauuidsetup(LoginEvent event) {

		if(!ConfigUtils.getConfBool("setup-uuid")) {
			return;
		}

		/*

		(Optional in ? config)
		1.6.3: Now the uuid will be changed only if you have premium login activated.
		(Optional)

		 */

		if(ConfigUtils.getConfBool("setup-only-if-active")) {
			if(!ConfigUtils.hasPremiumAutoLogin(event.getConnection().getName())) {
				return;
			}
		}

		String metodo = ConfigUtils.getConfStr("setup-method");

		PendingConnection connection = event.getConnection();
		try {

			String spud = null;

			if(metodo.equalsIgnoreCase("Sp")) { spud = UUIDUtils.getCrackedUUID(connection.getName()); }
			if(spud == null) {
				PremiumLoginMain.i().logConsole("UUID Parser: You set an invaild setup method into the configuration. So, the uuid will be parsed with default method.");
				spud = UUIDUtils.getCrackedUUID(connection.getName());
			}
			String oldUUID = event.getConnection().getUniqueId().toString();
			UUID nuovoUUID = UUID.fromString(spud);
			try {
				UUIDSetupEvent uuidEvent = PremiumLoginMain.i().getProxy().getPluginManager().callEvent(new UUIDSetupEvent(SetupMethod.SP, connection, nuovoUUID.toString(), oldUUID, 1));
			    if(uuidEvent.isCancelled()) {
					PremiumLoginMain.i().logConsole("WARNING - Another plugin cancelled the UUIDSetupEvent, the uuid setup has not not performed to this player.");
					return;
				}
			} catch(Exception exc) {
				PremiumLoginMain.i().logConsole("WARNING - Unable to fire the UUIDSetupEvent for API Plugins.");
			}
			Class<?> initialHandlerClass = connection.getClass();
			Field uniqueIdField = initialHandlerClass.getDeclaredField("uniqueId");
			uniqueIdField.setAccessible(true);
			uniqueIdField.set(connection, nuovoUUID);
			PremiumLoginMain.i().logConsole("Successfully set " + connection.getName() + "'s UUID.");
		} catch (Exception exc) {
			exc.printStackTrace();
			PremiumLoginMain.i().logConsole("Unable to setup " + connection.getName() + "'s UUID.");
			connection.disconnect(new TextComponent("§cUnable to setup your account's UUID. Please retry."));
			return;
		}
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void entrata(PostLoginEvent event) {
		final ProxiedPlayer p = event.getPlayer();


		//1.2 - Now event is async.
		//1.2 - Fixed console spam.
		PremiumLoginMain.i().getProxy().getScheduler().runAsync(PremiumLoginMain.i(), new Runnable() {

			@Override
			public void run() {
				try {
					if (!ConfigUtils.hasPremiumAutoLogin(p)) {
						return;
					}

					if (!UUIDUtils.isPremium(p)) {
						ConfigUtils.disablePremium(p);
						return;
					}

					if (p.isConnected()) {
						if (ConfigUtils.allowLegacy()) {
							if (!UUIDUtils.isPremiumConnectionLegacy(p) || !UUIDUtils.isPremiumConnection(p)) {
								PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
								PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
								p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("join-with-premium"))));
							} else if (!UUIDUtils.isPremiumConnectionLegacy(p) && UUIDUtils.isPremiumConnection(p)) {
								PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.NOTLEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
										.setName(p.getName())
										.setConnectionType(TipoConnessione.NOTLEGACY)
										.setPlayer(p)
										.setUUID(p.getUniqueId())
										.setServer(p.getServer().getInfo())
										.setAuthType(AuthType.AUTO)
										.build();
								if (AuthenticationHandler.login(p, key) != 1) {
									PluginUtils.send(p, ConfigUtils.getConfStr("unable"));
								}
							} else if (UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
								PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.LEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
										.setName(p.getName())
										.setConnectionType(TipoConnessione.LEGACY)
										.setPlayer(p)
										.setUUID(p.getUniqueId())
										.setServer(p.getServer().getInfo())
										.setAuthType(AuthType.AUTO)
										.build();
								if (AuthenticationHandler.login(p, key) != 1) {
									PluginUtils.send(p, ConfigUtils.getConfStr("error-generic"));
								}
							}
						} else {
							if (UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy() || !UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy()) {
								PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
								PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium") + " §c(Legacy Not Supported)§7.", c);
								p.disconnect(new TextComponent(ConfigUtils.getConfStr("no-legacy")));
							} else if (!UUIDUtils.isPremiumConnection(p)) {
								PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
								PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
								p.disconnect(new TextComponent(ConfigUtils.getConfStr("join-with-premium")));
							} else if (UUIDUtils.isPremiumConnection(p) && !p.getPendingConnection().isLegacy()) {
								PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.NOTLEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
										.setName(p.getName())
										.setConnectionType(TipoConnessione.NOTLEGACY)
										.setPlayer(p)
										.setUUID(p.getUniqueId())
										.setServer(p.getServer().getInfo())
										.setAuthType(AuthType.AUTO)
										.build();
								if (AuthenticationHandler.login(p, key) != 1) {
									PluginUtils.send(p, ConfigUtils.getConfStr("unable"));
								}
							}
						}
					}
				} catch (Exception exc) {
					//PluginUtils.send(p, ConfigUtils.getConfStr("unable"));
					PremiumLoginMain.i().getLogger().severe("ERROR - Unable to autologin " + p.getName() + " (ignore this, this is not an error [sometimes], is just for block console spam.)");
				}
			}
		});
	}


	@EventHandler
	public void switchEvent(ServerConnectedEvent event) {
		ProxiedPlayer p = event.getPlayer();

		if (!ConfigUtils.hasPremiumAutoLogin(p)) {
			return;
		}
		if (ConfigUtils.getConfBool("all-servers-are-server-auth")) {
			if (!UUIDUtils.isPremium(p)) {
				ConfigUtils.disablePremium(p);
				return;
			}
			if (p.isConnected()) {
				if (ConfigUtils.allowLegacy()) {
					if (!UUIDUtils.isPremiumConnectionLegacy(p) || !UUIDUtils.isPremiumConnection(p)) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
						p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("join-with-premium"))));
						return;
					} else if (!UUIDUtils.isPremiumConnectionLegacy(p) && UUIDUtils.isPremiumConnection(p)) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(TipoConnessione.LEGACY)
								.setPlayer(p)
								.setUUID(p.getUniqueId())
								.setServer(event.getServer().getInfo())
								.setAuthType(AuthType.AUTO)
								.build();
						if (AuthenticationHandler.login(p, key) != 1) {
							PluginUtils.send(p, ConfigUtils.getConfStr("error-generic"));
						}
					} else if (UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(TipoConnessione.LEGACY)
								.setPlayer(p)
								.setUUID(p.getUniqueId())
								.setServer(event.getServer().getInfo())
								.setAuthType(AuthType.AUTO)
								.build();
						if (AuthenticationHandler.login(p, key) != 1) {
							PluginUtils.send(p, ConfigUtils.getConfStr("error-generic"));
						}
					}
				} else {
					if (UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy() || !UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy()) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium") + " §c(Legacy Not Supported)§7.", c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("no-legacy")));
						return;
					} else if (!UUIDUtils.isPremiumConnection(p)) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("join-with-premium")));
						return;
					} else if (UUIDUtils.isPremiumConnection(p) && !p.getPendingConnection().isLegacy()) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(TipoConnessione.NOTLEGACY)
								.setPlayer(p)
								.setUUID(p.getUniqueId())
								.setServer(event.getServer().getInfo())
								.setAuthType(AuthType.AUTO)
								.build();
						if (AuthenticationHandler.login(p, key) != 1) {
							PluginUtils.send(p, ConfigUtils.getConfStr("unable"));
						}
					}
				}
			} else {
				return;
			}
			return;
		}
		String serverDiAutenticazione = ConfigUtils.getConfStr("auth-server");
		if (event.getServer().getInfo().getName().equalsIgnoreCase(serverDiAutenticazione)) {
			if (!UUIDUtils.isPremium(p)) {
				ConfigUtils.disablePremium(p);
				return;
			}
			if (p.isConnected()) {
				if (ConfigUtils.allowLegacy()) {
					if (!UUIDUtils.isPremiumConnectionLegacy(p) || !UUIDUtils.isPremiumConnection(p)) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
						p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("join-with-premium"))));
						return;
					} else if (!UUIDUtils.isPremiumConnectionLegacy(p) && UUIDUtils.isPremiumConnection(p)) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(TipoConnessione.LEGACY)
								.setPlayer(p)
								.setUUID(p.getUniqueId())
								.setServer(event.getServer().getInfo())
								.setAuthType(AuthType.AUTO)
								.build();
						if (AuthenticationHandler.login(p, key) != 1) {
							PluginUtils.send(p, ConfigUtils.getConfStr("error-generic"));
						}
					} else if (UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(TipoConnessione.LEGACY)
								.setPlayer(p)
								.setUUID(p.getUniqueId())
								.setServer(event.getServer().getInfo())
								.setAuthType(AuthType.AUTO)
								.build();
						if (AuthenticationHandler.login(p, key) != 1) {
							PluginUtils.send(p, ConfigUtils.getConfStr("error-generic"));
						}
					}
				} else {
					if (UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy() || !UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy()) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium") + " §c(Legacy Not Supported)§7.", c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("no-legacy")));
					} else if (!UUIDUtils.isPremiumConnection(p)) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						PluginUtils.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("join-with-premium")));
					} else if (UUIDUtils.isPremiumConnection(p) && !p.getPendingConnection().isLegacy()) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(TipoConnessione.NOTLEGACY)
								.setPlayer(p)
								.setUUID(p.getUniqueId())
								.setServer(event.getServer().getInfo())
								.setAuthType(AuthType.AUTO)
								.build();
						if (AuthenticationHandler.login(p, key) != 1) {
							PluginUtils.send(p, ConfigUtils.getConfStr("unable"));
						}
					}
				}
			} else {
				return;
			}
		}
	}

	@EventHandler
	public void uscita(PlayerDisconnectEvent event) {
		if (ConfigUtils.hasPremiumAutoLogin(event.getPlayer().getName()))
			PremiumLoginEventManager.fire(new PremiumQuitEvent(event.getPlayer()));
	}


}
