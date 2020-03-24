package it.notreference.bungee.premiumlogin.commands;

//import it.notreference.bungee.premiumlogin.utils.AuthKey;
//import it.notreference.bungee.premiumlogin.utils.AuthType;
//import it.notreference.bungee.premiumlogin.utils.AuthUtils;
//import it.notreference.bungee.premiumlogin.utils.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
//import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * PremiumLogin 1.3 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PremiumCmd extends Command{

	public PremiumCmd() {
		super("premium");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent("§cERROR - You must be a player."));
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(ConfigUtils.permessoSettato()) {
			if(!p.hasPermission(ConfigUtils.getPremiumPerm())) {
				Messages.sendParseColors(p, ConfigUtils.getConfStr("no-perms"));
				return;
			} 
		}
		
		if(!UUIDUtils.isPremium(p)) {
			Messages.send(p, ConfigUtils.getConfStr("no-premium"));
			return;
		}
		
		if(ConfigUtils.hasPremiumAutoLogin(p)) {
			try {
			ConfigUtils.disablePremium(p);
			ConfigUtils.player_save();
			} catch(Exception exc) {
				 p.sendMessage(new TextComponent(Messages.parse(ConfigUtils.getConfStr("error-generic"))));
				return;
			}
			Messages.send(p, ConfigUtils.getConfStr("disable-autologin"));
			Messages.send(p, ConfigUtils.getConfStr("default-login-system-switch-to-authme"));
			p.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("disable-autologin"))));
			return;
		}
		
			ConfigUtils.enablePremium(p);
			ConfigUtils.player_save();
			ConfigUtils.player_reload();
			p.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("enabled-autologin"))));
			return;
			
			//Codice morto:
			
			
			
			//vai tranquillo
			//if(ConfigUtils.allowLegacy()) {
				//if(!AuthUtils.isPremiumLogged(p) && !ConfigUtils.hasPremiumAutoLogin(p)) {
					//ConfigUtils.enablePremium(p);
					//ConfigUtils.salva();
					//Messages.send(p, ConfigUtils.getConfStr("enabled-autologin"));
					//Messages.send(p, ConfigUtils.getConfStr("default-login-system-switch-to-premiumlogin"));
					//if(UUIDVerify.isPremiumConnection(p)) {
					//AuthKey k = new AuthenticationBuilder()
					  //             .setName(p.getName())
//					               .setAuthType(AuthType.COMMAND)
	//				               .setConnectionType(TipoConnessione.NOTLEGACY)
		//			               .setServer(p.getServer().getInfo())
			//		               .setUUID(p.getUniqueId())
			//		               .setPlayer(p)
			//	               .build();
				//	AuthUtils.login(p, k);
					//} else if(UUIDVerify.isPremiumConnectionLegacy(p)){
						//AuthKey k = new AuthenticationBuilder()
			              // .setName(p.getName())
			               //.setAuthType(AuthType.COMMAND)
			               //.setConnectionType(TipoConnessione.LEGACY)
			               //.setServer(p.getServer().getInfo())
			               //.setUUID(p.getUniqueId())
			               //.setPlayer(p)
			               //.build();
			           //AuthUtils.login(p, k);
					//}
					//return;
				//}
				//} else {
					//if(p.getPendingConnection().isLegacy()) {
						//Messages.send(p, ConfigUtils.getConfStr("no-legacy"));
						//return;
					//}
					//if(!AuthUtils.isPremiumLogged(p) && !ConfigUtils.hasPremiumAutoLogin(p)) {
						//ConfigUtils.enablePremium(p);
						//Messages.send(p, ConfigUtils.getConfStr("enabled-autologin"));
						//Messages.send(p, ConfigUtils.getConfStr("default-login-system-switch-to-premiumlogin"));
						//AuthKey k = new AuthenticationBuilder()
			              // .setName(p.getName())
			               // .setAuthType(AuthType.COMMAND)
			               // .setConnectionType(TipoConnessione.LEGACY)
			               // .setServer(p.getServer().getInfo())
			               // .setUUID(p.getUniqueId())
			               // .setPlayer(p)
			               // .build();
			           //AuthUtils.login(p, k);
					//return;
			//	}
			//	}
		
	

}
}

