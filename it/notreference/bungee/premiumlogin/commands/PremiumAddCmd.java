package it.notreference.bungee.premiumlogin.commands;

//import it.notreference.bungee.premiumlogin.utils.AuthKey;
//import it.notreference.bungee.premiumlogin.utils.AuthType;
//import it.notreference.bungee.premiumlogin.utils.AuthUtils;
//import it.notreference.bungee.premiumlogin.utils.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
//import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * PremiumLogin 1.5 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PremiumAddCmd extends Command{

	public PremiumAddCmd() {
		super("premiumadd");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer))  {
			try {
			CommandSender p = sender;
			if(args.length != 1) {
				Messages.send(p, "§7Correct Usage: §f/premiumadd (player) §7- §bAdd a player to premium players list.");
				return;
			}
			
		
			
			if(ConfigUtils.hasPremiumAutoLogin(args[0])) {
				Messages.send(p, "§cThis player is arleady in premium list.");
			} else {
			
				if(!UUIDUtils.isPremium(args[0])) {
					Messages.send(p, "§cThis player is not premium.");
					return;
				}
				
				ConfigUtils.enablePremium(args[0]);
				ConfigUtils.player_save();
				ConfigUtils.player_reload();
				
				Messages.send(p, "§aSuccessfully enabled PremiumLogin to " + args[0]);
				
				try {
					ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
				if(target.isConnected()) {
				target.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("enabled-autologin"))));
				}
				} catch(Exception exc) {
					Messages.send(p, "§aSuccessfully enabled PremiumLogin to " + args[0]);
				}
				return;
			}
			} catch(Exception exc) {
				sender.sendMessage(new TextComponent("§cUnable to do this action."));
			}
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		
		try {
		
			if(!p.hasPermission("premiumlogin.staff")) {
				Messages.sendParseColors(p, ConfigUtils.getConfStr("no-perms"));
				return;
			} 
	
		if(args.length != 1) {
			Messages.send(p, "§7Correct Usage: §f/premiumadd (player) §7- §bAdd a player to premium players list.");
			return;
		}
		
		
		if(ConfigUtils.hasPremiumAutoLogin(args[0])) {
			Messages.send(p, "§cThis player is arleady in premium list.");
		} else {
		
			if(!UUIDUtils.isPremium(args[0])) {
				Messages.send(p, "§cThis player is not premium.");
				return;
			}
			
			ConfigUtils.enablePremium(args[0]);
			ConfigUtils.player_save();
			ConfigUtils.player_reload();
			
			Messages.send(p, "§aSuccessfully enabled PremiumLogin to " + args[0]);
			try {
				ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
			if(target.isConnected()) {
			target.disconnect(new TextComponent(Messages.parse(ConfigUtils.getConfStr("enabled-autologin"))));
			}
			} catch(Exception exc) {
				Messages.send(p, "§aSuccessfully enabled PremiumLogin to " + args[0]);
			}
			return;
		}
		} catch(Exception exc) {
			Messages.send(p, "§cUnable to do this action.");
		}

}
}

