package it.notreference.bungee.premiumlogin.listeners;

//import java.lang.reflect.Field;
//import java.nio.charset.Charset;
import java.lang.reflect.Field;
import java.util.UUID;

import it.notreference.bungee.premiumlogin.EventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.Updater;
import it.notreference.bungee.premiumlogin.api.events.PremiumJoinEvent;
import it.notreference.bungee.premiumlogin.api.events.PremiumQuitEvent;
import it.notreference.bungee.premiumlogin.authentication.AuthType;
import it.notreference.bungee.premiumlogin.authentication.AuthenticationHandler;
import it.notreference.bungee.premiumlogin.authentication.AuthenticationKey;
import it.notreference.bungee.premiumlogin.authentication.TipoConnessione;
import it.notreference.bungee.premiumlogin.authentication.utils.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import it.notreference.bungee.premiumlogin.utils.PlaceholderConf;
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

public class Eventi implements Listener{

	@EventHandler(priority = 7)
    public void connectionsetup(PreLoginEvent event) {
        PendingConnection connection = event.getConnection();

        if (ConfigUtils.hasPremiumAutoLogin(connection.getName())) {
            connection.setOnlineMode(true);
            PremiumLoginMain.i().logConsole("Successfully set " + connection.getName() + "'s connection to Premium.");
        }
    }

	
	@EventHandler
	public void entratauuidsetup(LoginEvent event) {
		PendingConnection connection = event.getConnection();
		 try {
			 String spud = UUIDUtils.getCrackedUUID(connection.getName());
				UUID nuovoUUID = UUID.fromString(spud);
				Class<?> initialHandlerClass = connection.getClass();
					Field uniqueIdField = initialHandlerClass.getDeclaredField("uniqueId");
					uniqueIdField.setAccessible(true);
					uniqueIdField.set(connection, nuovoUUID);
             PremiumLoginMain.i().logConsole("Successfully set " + connection.getName() + "'s UUID.");
         } catch (Exception exc) {
        	   PremiumLoginMain.i().logConsole("Unable to setup " + connection.getName() + "'s UUID.");
        	   exc.printStackTrace();
             connection.disconnect(new TextComponent("§cUnable to setup your account's UUID. Please retry."));
             return;
         }
	}
	
	@EventHandler
	public void entrata(PostLoginEvent event) {
		ProxiedPlayer p = event.getPlayer();
		
		
		//1.2 - Now event is async.
		//1.2 - Fixed console spam.
		PremiumLoginMain.i().getProxy().getScheduler().runAsync(PremiumLoginMain.i(), new Runnable() {

			@Override
			public void run() {
				try {
					if(!ConfigUtils.hasPremiumAutoLogin(p)) {
						return;
					}
					
						if(!UUIDUtils.isPremium(p)) {
							ConfigUtils.disablePremium(p);
							return;
						}
						
						if(p.isConnected()) {
							if(ConfigUtils.allowLegacy()) {
							if(!UUIDUtils.isPremiumConnectionLegacy(p) || !UUIDUtils.isPremiumConnection(p)) {
								PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
								Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
								p.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("join-with-premium"))));
								return;
							} else if(!UUIDUtils.isPremiumConnectionLegacy(p) && UUIDUtils.isPremiumConnection(p)) {
								//1
								EventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.NOTLEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
								                     .setName(p.getName())
								                 .setConnectionType(TipoConnessione.NOTLEGACY)
								                   .setPlayer(p)
								                   .setUUID(p.getUniqueId())
								                   .setServer(p.getServer().getInfo())
								                   .setAuthType(AuthType.AUTO)
								                   .build();
								if(AuthenticationHandler.login(p, key) != 1) {
									Messages.send(p, ConfigUtils.getConfStr("unable"));
								} 
							} else if(UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
								//1
								EventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.LEGACY));
								AuthenticationKey key = new AuthenticationBuilder()
			                    .setName(p.getName())
			                .setConnectionType(TipoConnessione.LEGACY)
			                  .setPlayer(p)
			                  .setUUID(p.getUniqueId())
			                  .setServer(p.getServer().getInfo())
			                  .setAuthType(AuthType.AUTO)
			                  .build();
			if(AuthenticationHandler.login(p, key) != 1) {
				Messages.send(p, ConfigUtils.getConfStr("error-generic"));
			} 
							}
							} else {
								if(UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy() || !UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy()) {
									PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
									Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium") + " §c(Legacy Not Supported)§7.", c);
									p.disconnect(new TextComponent(ConfigUtils.getConfStr("no-legacy")));
									return;
								} else if(!UUIDUtils.isPremiumConnection(p)) {
									PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
									Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
									p.disconnect(new TextComponent(ConfigUtils.getConfStr("join-with-premium")));
									return;
								} else if(UUIDUtils.isPremiumConnection(p) && !p.getPendingConnection().isLegacy()) {
									//1
									EventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.NOTLEGACY));
									AuthenticationKey key = new AuthenticationBuilder()
				                     .setName(p.getName())
				                 .setConnectionType(TipoConnessione.NOTLEGACY)
				                   .setPlayer(p)
				                   .setUUID(p.getUniqueId())
				                   .setServer(p.getServer().getInfo())
				                   .setAuthType(AuthType.AUTO)
				                   .build();
				if(AuthenticationHandler.login(p, key) != 1) {
					Messages.send(p, ConfigUtils.getConfStr("unable"));
				}
								}
							}
						} else {
							return;
						}
					} catch(Exception exc) {
						Messages.send(p, ConfigUtils.getConfStr("unable"));
						PremiumLoginMain.i().getLogger().severe("ERROR - Unable to autologin " + p.getName() + " (ignore this, this is not an error [sometimes], is just for block console spam.)");
						return;
					}	
			}
			
			
			
			
		});
		
		//1.5 - Updater 
		PremiumLoginMain.i().getProxy().getScheduler().runAsync(PremiumLoginMain.i(), new Runnable() {
			
			@Override
			public void run() {
			
		Updater updater = new Updater(PremiumLoginMain.i());
		if(updater.isAvaliable()) {
		if(event.getPlayer().hasPermission("premiumlogin.staff")) {
		 event.getPlayer().sendMessage(new TextComponent
				 ("§6§l[PremiumLogin] §6A new plugin version is avaliable: " 
		         + updater.next() 
	             + ". "
				 + "§eDownload it now §ehttps://www.spigotmc.org/resources/premiumlogin.76336/§6."
				 ));
		}
		}
		}
		
		
		});
	}
	
	
	@EventHandler
	public void switchEvent(ServerConnectedEvent event) {
		ProxiedPlayer p = event.getPlayer();
		
		
		if(!ConfigUtils.hasPremiumAutoLogin(p)) {
			return;
		}
		if(ConfigUtils.getConfBool("all-servers-are-server-auth")) {
			if(!UUIDUtils.isPremium(p)) {
				ConfigUtils.disablePremium(p);
				return;
			}
			if(p.isConnected()) {
				if(ConfigUtils.allowLegacy()) {
				if(!UUIDUtils.isPremiumConnectionLegacy(p) || !UUIDUtils.isPremiumConnection(p)) {
					PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
					Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
					p.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("join-with-premium"))));
					return;
				} else if(!UUIDUtils.isPremiumConnectionLegacy(p) && UUIDUtils.isPremiumConnection(p)) {
					//1
					AuthenticationKey key = new AuthenticationBuilder()
					                     .setName(p.getName())
					                 .setConnectionType(TipoConnessione.LEGACY)
					                   .setPlayer(p)
					                   .setUUID(p.getUniqueId())
					                   .setServer(event.getServer().getInfo())
					                   .setAuthType(AuthType.AUTO)
					                   .build();
					if(AuthenticationHandler.login(p, key) != 1) {
						Messages.send(p, ConfigUtils.getConfStr("error-generic"));
					}
				} else if(UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
					//1
					AuthenticationKey key = new AuthenticationBuilder()
                    .setName(p.getName())
                .setConnectionType(TipoConnessione.LEGACY)
                  .setPlayer(p)
                  .setUUID(p.getUniqueId())
                  .setServer(event.getServer().getInfo())
                  .setAuthType(AuthType.AUTO)
                  .build();
if(AuthenticationHandler.login(p, key) != 1) {
	Messages.send(p, ConfigUtils.getConfStr("error-generic"));
}
				}
				} else {
					if(UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy() || !UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy()) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium") + " §c(Legacy Not Supported)§7.", c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("no-legacy")));
						return;
					} else if(!UUIDUtils.isPremiumConnection(p)) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("join-with-premium")));
						return;
					} else if(UUIDUtils.isPremiumConnection(p) && !p.getPendingConnection().isLegacy()) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
	                     .setName(p.getName())
	                 .setConnectionType(TipoConnessione.NOTLEGACY)
	                   .setPlayer(p)
	                   .setUUID(p.getUniqueId())
	                   .setServer(event.getServer().getInfo())
	                   .setAuthType(AuthType.AUTO)
	                   .build();
	if(AuthenticationHandler.login(p, key) != 1) {
		Messages.send(p, ConfigUtils.getConfStr("unable"));
	}
					}
				}
			} else {
				return;
			}
			return;
		}
		String serverDiAutenticazione = ConfigUtils.getConfStr("auth-server");
		if(event.getServer().getInfo().getName().equalsIgnoreCase(serverDiAutenticazione)) {
			if(!UUIDUtils.isPremium(p)) {
				ConfigUtils.disablePremium(p);
				return;
			}
			if(p.isConnected()) {
				if(ConfigUtils.allowLegacy()) {
				if(!UUIDUtils.isPremiumConnectionLegacy(p) || !UUIDUtils.isPremiumConnection(p)) {
					PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
					Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
					p.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("join-with-premium"))));
					return;
				} else if(!UUIDUtils.isPremiumConnectionLegacy(p) && UUIDUtils.isPremiumConnection(p)) {
					//1
					AuthenticationKey key = new AuthenticationBuilder()
					                     .setName(p.getName())
					                 .setConnectionType(TipoConnessione.LEGACY)
					                   .setPlayer(p)
					                   .setUUID(p.getUniqueId())
					                   .setServer(event.getServer().getInfo())
					                   .setAuthType(AuthType.AUTO)
					                   .build();
					if(AuthenticationHandler.login(p, key) != 1) {
						Messages.send(p, ConfigUtils.getConfStr("error-generic"));
					}
				} else if(UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
					//1
					AuthenticationKey key = new AuthenticationBuilder()
                    .setName(p.getName())
                .setConnectionType(TipoConnessione.LEGACY)
                  .setPlayer(p)
                  .setUUID(p.getUniqueId())
                  .setServer(event.getServer().getInfo())
                  .setAuthType(AuthType.AUTO)
                  .build();
if(AuthenticationHandler.login(p, key) != 1) {
	Messages.send(p, ConfigUtils.getConfStr("error-generic"));
}
				}
				} else {
					if(UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy() || !UUIDUtils.isPremiumConnection(p) && p.getPendingConnection().isLegacy()) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium") + " §c(Legacy Not Supported)§7.", c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("no-legacy")));
						return;
					} else if(!UUIDUtils.isPremiumConnection(p)) {
						PlaceholderConf c = new PlaceholderConf(p.getName(), p.getUniqueId(), p.getAddress().getHostString());
						Messages.logStaff(ConfigUtils.getConfStr("try-join-nopremium"), c);
						p.disconnect(new TextComponent(ConfigUtils.getConfStr("join-with-premium")));
						return;
					} else if(UUIDUtils.isPremiumConnection(p) && !p.getPendingConnection().isLegacy()) {
						//1
						AuthenticationKey key = new AuthenticationBuilder()
	                     .setName(p.getName())
	                 .setConnectionType(TipoConnessione.NOTLEGACY)
	                   .setPlayer(p)
	                   .setUUID(p.getUniqueId())
	                   .setServer(event.getServer().getInfo())
	                   .setAuthType(AuthType.AUTO)
	                   .build();
	if(AuthenticationHandler.login(p, key) != 1) {
		Messages.send(p, ConfigUtils.getConfStr("unable"));
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
		
		//1.5 - Fixed the event fire bug.
		
		if(ConfigUtils.hasPremiumAutoLogin(event.getPlayer().getName()))
		EventManager.fire(new PremiumQuitEvent(event.getPlayer()));
	}
	
	
	
}
