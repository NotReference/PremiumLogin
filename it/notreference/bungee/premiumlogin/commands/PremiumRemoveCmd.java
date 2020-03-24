package it.notreference.bungee.premiumlogin.commands;

//import it.notreference.bungee.premiumlogin.utils.AuthKey;
//import it.notreference.bungee.premiumlogin.utils.AuthType;
//import it.notreference.bungee.premiumlogin.utils.AuthUtils;
//import it.notreference.bungee.premiumlogin.utils.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
//import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
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

public class PremiumRemoveCmd extends Command{

	public PremiumRemoveCmd() {
		super("premiumremove");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			CommandSender p = sender;
			if(args.length != 1) {
				Messages.send(p, "§7Correct Usage: §f/premiumremove (player) §7- §bAdd a player to premium players list.");
				return;
			}
			
			if(!ConfigUtils.hasPremiumAutoLogin(args[0])) {
				Messages.send(p, "§cThis player is not in premium list.");
			} else {
			
				ConfigUtils.disablePremium(args[0]);
				ConfigUtils.player_save();
				ConfigUtils.player_reload();
				
				Messages.send(p, "§aSuccessfully disabled PremiumLogin to " + args[0]);
				
				try {
				
					ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
					
				if(target.isConnected() && target != null) {
				target.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("staff-disable"))));
				}
				} catch(Exception exc) {
					Messages.send(p, "§aSuccessfully disabled PremiumLogin to " + args[0]);
				}
				return;
			}
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(!p.hasPermission("premiumlogin.staff")) {
			Messages.sendParseColors(p, ConfigUtils.getConfStr("no-perms"));
			return;
		} 

	if(args.length != 1) {
		Messages.send(p, "§7Correct Usage: §f/premiumremove (player) §7- §bAdd a player to premium players list.");
		return;
	}
	
	
	
	if(!ConfigUtils.hasPremiumAutoLogin(args[0])) {
		Messages.send(p, "§cThis player is not in premium list.");
	} else {
	
		ConfigUtils.disablePremium(args[0]);
		ConfigUtils.player_save();
		ConfigUtils.player_reload();
		
		Messages.send(p, "§aSuccessfully disabled PremiumLogin to " + args[0]);
		
		try {
			ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
		if(target.isConnected() && target != null) {
		target.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("staff-disable"))));
		}
		} catch(Exception exc) {
			Messages.send(p, "§aSuccessfully disabled PremiumLogin to " + args[0]);
		}
		return;
	}
		

}
}

