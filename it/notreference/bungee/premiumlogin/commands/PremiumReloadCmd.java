package it.notreference.bungee.premiumlogin.commands;

//import it.notreference.bungee.premiumlogin.utils.AuthUtils;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;



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


public class PremiumReloadCmd extends Command {

	public PremiumReloadCmd() {
		super("premiumreload");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			ConfigUtils.reload();
			ConfigUtils.player_reload();
			Messages.send(sender, "§aConfiguration successfully reloaded.");
			return;
		}
		
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
