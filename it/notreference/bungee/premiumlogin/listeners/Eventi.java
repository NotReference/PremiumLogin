package it.notreference.bungee.premiumlogin.listeners;

//import java.lang.reflect.Field;
//import java.nio.charset.Charset;
import java.lang.reflect.Field;
import java.util.UUID;

import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.events.PremiumJoinEvent;
import it.notreference.bungee.premiumlogin.api.events.PremiumQuitEvent;
import it.notreference.bungee.premiumlogin.utils.AuthenticationKey;
import it.notreference.bungee.premiumlogin.utils.AuthType;
import it.notreference.bungee.premiumlogin.utils.AuthenticationHandler;
import it.notreference.bungee.premiumlogin.utils.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
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

/**
 * PremiumLogin 1.1 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
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
        	   PremiumLoginMain.i().logConsole("Cannot setup " + connection.getName() + "'s UUID.");
        	   exc.printStackTrace();
             connection.disconnect(new TextComponent("§cCannot setup your account's UUID. Please retry."));
             return;
         }
	}
	
	@EventHandler
	public void entrata(PostLoginEvent event) {
		//PendingConnection connection = event.getPlayer().getPendingConnection();
		 
		ProxiedPlayer p = event.getPlayer();
		
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
					PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.NOTLEGACY));
					AuthenticationKey key = new AuthenticationBuilder()
					                     .setName(p.getName())
					                 .setConnectionType(TipoConnessione.NOTLEGACY)
					                   .setPlayer(p)
					                   .setUUID(p.getUniqueId())
					                   .setServer(p.getServer().getInfo())
					                   .setAuthType(AuthType.AUTO)
					                   .build();
					if(AuthenticationHandler.login(p, key) != 1) {
						Messages.send(p, ConfigUtils.getConfStr("error-generic"));
					} 
				} else if(UUIDUtils.isPremiumConnectionLegacy(p) && !UUIDUtils.isPremiumConnection(p)) {
					//1
					PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.LEGACY));
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
						PremiumLoginEventManager.fire(new PremiumJoinEvent(p, p.getName(), p.getUniqueId(), p.getAddress(), TipoConnessione.NOTLEGACY));
						AuthenticationKey key = new AuthenticationBuilder()
	                     .setName(p.getName())
	                 .setConnectionType(TipoConnessione.NOTLEGACY)
	                   .setPlayer(p)
	                   .setUUID(p.getUniqueId())
	                   .setServer(p.getServer().getInfo())
	                   .setAuthType(AuthType.AUTO)
	                   .build();
	if(AuthenticationHandler.login(p, key) != 1) {
		Messages.send(p, ConfigUtils.getConfStr("error-generic"));
	}
					}
				}
			} else {
				return;
			}
			}
	
	
	@EventHandler
	public void switchEvent(ServerConnectedEvent event) {
		ProxiedPlayer p = event.getPlayer();
		//if(AuthUtils.isPremiumLogged(p) //&& !event.getServer().getInfo().getName().equalsIgnoreCase(ConfigUtils.getConfStr("auth-server"))
			//	) {
			//AuthUtils.getLogged().remove(p);
		//}
		
		
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
		Messages.send(p, ConfigUtils.getConfStr("error-generic"));
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
		Messages.send(p, ConfigUtils.getConfStr("error-generic"));
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
		//ProxiedPlayer p = event.getPlayer();
		//if(AuthUtils.isPremiumLogged(p)) {
			//AuthUtils.getLogged().remove(p);
		//} 
		if(UUIDUtils.isPremium(event.getPlayer()) && UUIDUtils.isPremiumConnection(event.getPlayer()) || (ConfigUtils.allowLegacy() && UUIDUtils.isPremiumConnectionLegacy(event.getPlayer())))
		PremiumLoginEventManager.fire(new PremiumQuitEvent(event.getPlayer()));

	}
	
	
	
}
