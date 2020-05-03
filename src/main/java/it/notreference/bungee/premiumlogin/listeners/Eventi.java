package it.notreference.bungee.premiumlogin.listeners;

//import java.lang.reflect.Field;
//import java.nio.charset.Charset;
import java.lang.reflect.Field;
import java.util.UUID;

import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.PremiumOnlineConnection;
import it.notreference.bungee.premiumlogin.api.SetupMethod;
import it.notreference.bungee.premiumlogin.api.events.PremiumJoinEvent;
import it.notreference.bungee.premiumlogin.api.events.PremiumQuitEvent;
import it.notreference.bungee.premiumlogin.api.events.UUIDSetupEvent;
import it.notreference.bungee.premiumlogin.utils.*;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthType;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationHandler;
import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.data.DataProvider;
import it.notreference.bungee.premiumlogin.utils.data.PlayerData;
import it.notreference.bungee.premiumlogin.utils.data.PlayerDataHandler;
import it.notreference.bungee.premiumlogin.utils.data.PlayerDataManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;


/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class Eventi implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void connectionSetup(PreLoginEvent event) {
		if(event.isCancelled()) {
			return;
		}
		PendingConnection connection = event.getConnection();

		for(ProxiedPlayer player : PremiumLoginMain.i().getProxy().getPlayers())  {
			if(!player.getName().equals(connection.getName()) && player.getAddress().equals(connection.getAddress())) {
				if(ConfigUtils.getConfBool("2-sessions-kick")) {
					connection.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("multiple-ips".replace("{user}", player.getName())))));
					return;
				}
			}
		}

		if (ConfigUtils.hasPremiumAutoLogin(connection.getName())) {
			if(!UUIDUtils.isPremium(connection.getName())) {
				ConfigUtils.disablePremium(connection.getName());
				connection.disconnect(new TextComponent("&cYou are not a Premium Player. Please rejoin. Your profile data has been refreshed."));
				return;
			}
			try {
				connection.setOnlineMode(true);
				PremiumLoginMain.i().logConsole("Successfully set " + connection.getName() + "'s connection to Premium.");
			} catch(Exception exc) {
				connection.disconnect(new TextComponent("§cUnable to join. Session server returned an invaild response. Please retry."));
				PremiumLoginMain.i().getLogger().severe("ERROR - Unable to set " + connection.getName() + " to Premium. (Maybe sessions server offline?)");
			}
		}
	}


	@EventHandler (priority = EventPriority.HIGHEST)
	public void uuidSetup(LoginEvent event) {

		if(event.isCancelled()) {
			return;
		}

		if(!ConfigUtils.getConfBool("setup-uuid")) {
			return;
		}

		/*

		1.7: Now the uuid will be changed only if you have premium login activated.


		 */

		PendingConnection connection = event.getConnection();

			if(!ConfigUtils.hasPremiumAutoLogin(event.getConnection().getName())) {
				return;
			}

			if(!connection.isOnlineMode()) {
				return;
			}


		String metodo = ConfigUtils.getConfStr("setup-method");
		if(!metodo.equalsIgnoreCase("sp"))
		{
			PremiumLoginMain.i().logConsole("Successfully set " + event.getConnection().getName() + "'s UUID to PREMIUM.");
			return;
		}

		try {

			String spud = null;
            spud = UUIDUtils.getCrackedUUID(connection.getName());
			if(spud == null) {
				PremiumLoginMain.i().logConsole("UUID Parser: You set an invaild setup method into the configuration. So, the uuid will be parsed with default method.");
				spud = UUIDUtils.getCrackedUUID(connection.getName());
			}
			String oldUUID = event.getConnection().getUniqueId().toString();
			UUID nuovoUUID = UUID.fromString(spud);
			try {
				UUIDSetupEvent uuidEvent = PremiumLoginMain.i().getProxy().getPluginManager().callEvent(new UUIDSetupEvent(SetupMethod.SP, connection, nuovoUUID.toString(), oldUUID, 1));
			} catch(Exception exc) {
				PremiumLoginMain.i().logConsole("WARNING - Unable to fire the UUIDSetupEvent for API Plugins.");
			}

			InitialHandler handler = (InitialHandler) connection;
			Class<?> initialHandlerClass =  handler.getClass(); //connection.getClass();
			Field uniqueIdField = initialHandlerClass.getDeclaredField("uniqueId");
			uniqueIdField.setAccessible(true);
			uniqueIdField.set(connection, nuovoUUID);
			PremiumLoginMain.i().logConsole("Successfully set " + connection.getName() + "'s UUID to SP.");
		} catch (Exception exc) {
			exc.printStackTrace();
			PremiumLoginMain.i().logConsole("Unable to setup " + connection.getName() + "'s UUID.");
			connection.disconnect(new TextComponent("§cUnable to setup your account's UUID. Please retry."));
			return;
		}
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onJoin(PostLoginEvent event) {
		final ProxiedPlayer p = event.getPlayer();

		/*

		Coming Soon!

		 */
		/*
		boolean continueCode1 = false;

		PlayerDataHandler handler = DataProvider.get(PlayerDataManager.class);

		if(!handler.init()) {

			if(!ConfigUtils.getConfBool("ignore-data-errors")) {

				p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("unable-data-init"))));
				return;

			} else {

				continueCode1 = true;

			}

		}

		if(!continueCode1) {
			if (handler.exists(p.getName())) {

				if(!handler.scanData(p.getName())) {
					//data corrupt
					return;
				} else {

					PlayerData playerData = handler.loadData(p.getName());
					if (playerData == null) {
						//kick null error data
					}
				}
			} else {

				//Creiamo la data.

			}
		}
		 */



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
								PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), ConnectionType.NOTLEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
										.setName(p.getName())
										.setConnectionType(ConnectionType.NOTLEGACY)
										.setPlayer(p)
										.setUUID(p.getUniqueId())
										.setServer(p.getServer().getInfo())
										.setAuthType(AuthType.AUTO)
										.build();
								if (AuthenticationHandler.login(p, key) != 1) {
									PluginUtils.send(p, ConfigUtils.getConfStr("unable"));
								}
							} else if (UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
								PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), ConnectionType.LEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
										.setName(p.getName())
										.setConnectionType(ConnectionType.LEGACY)
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
								PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), ConnectionType.NOTLEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
										.setName(p.getName())
										.setConnectionType(ConnectionType.NOTLEGACY)
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

						if(PremiumLoginMain.i().isPremiumConnected(p.getName())) {

                       p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("2-sessions-kick"))));
                       return;

						}

						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(ConnectionType.LEGACY)
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

						if(PremiumLoginMain.i().isPremiumConnected(p.getName())) {

							p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("2-sessions-kick"))));
							return;

						}

						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(ConnectionType.LEGACY)
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

						if(PremiumLoginMain.i().isPremiumConnected(p.getName())) {

							p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("2-sessions-kick"))));
							return;

						}

						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(ConnectionType.NOTLEGACY)
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
						if(PremiumLoginMain.i().isPremiumConnected(p.getName())) {

							p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("2-sessions-kick"))));
							return;

						}

						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(ConnectionType.LEGACY)
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

						if(PremiumLoginMain.i().isPremiumConnected(p.getName())) {

							p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("2-sessions-kick"))));
							return;

						}

						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(ConnectionType.LEGACY)
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

						if(PremiumLoginMain.i().isPremiumConnected(p.getName())) {

							p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("2-sessions-kick"))));
							return;

						}

						AuthenticationKey key = new AuthenticationBuilder()
								.setName(p.getName())
								.setConnectionType(ConnectionType.NOTLEGACY)
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
		if (ConfigUtils.hasPremiumAutoLogin(event.getPlayer().getName())) {
			PremiumLoginMain.i().getProxy().getScheduler().runAsync(PremiumLoginMain.i(), () -> {
				for (PremiumOnlineConnection con : PremiumLoginMain.i().getPremiumConnections()) {
					if (con.getPlayerName().equals(event.getPlayer().getName()))
						PremiumLoginMain.i().removeConnection(con);
				}
			});
			PremiumLoginEventManager.fire(new PremiumQuitEvent(event.getPlayer()));
		}
	}


}
