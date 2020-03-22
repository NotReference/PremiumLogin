package it.notreference.bungee.premiumlogin.commands;

//import it.notreference.bungee.premiumlogin.utils.AuthUtils;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


/**
 * PremiumLogin 1.1 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */


public class PremiumReloadCmd extends Command {

	public PremiumReloadCmd() {
		super("premiumreload");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(p.hasPermission("premiumlogin.reload")) {
			ConfigUtils.reload();
			ConfigUtils.player_reload();
			//AuthUtils.clearLoggedIn();
			Messages.send(p, "§aConfiguration successfully reloaded.");
		} else {
			Messages.send(p, ConfigUtils.getConfStr("no-perms"));
		}
	}

}
