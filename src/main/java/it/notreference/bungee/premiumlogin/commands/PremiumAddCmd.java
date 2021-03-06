package it.notreference.bungee.premiumlogin.commands;

//import it.notreference.bungee.premiumlogin.utils.AuthKey;
//import it.notreference.bungee.premiumlogin.utils.authentication.AuthType;
//import it.notreference.bungee.premiumlogin.utils.AuthUtils;
//import it.notreference.bungee.premiumlogin.utils.authentication.AuthenticationBuilder;
import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.SwitchType;
import it.notreference.bungee.premiumlogin.api.events.PremiumStaffSwitchEvent;
import it.notreference.bungee.premiumlogin.api.events.PremiumSwitchEvent;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.PluginUtils;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
//import it.notreference.bungee.premiumlogin.utils.TipoConnessione;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;




/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
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
					PluginUtils.send(p, "�7Correct Usage: �f/premiumadd (player) �7- �bAdd a player to premium players list.");
					return;
				}



				if(ConfigUtils.hasPremiumAutoLogin(args[0])) {
					PluginUtils.send(p, "�cThis player is arleady in premium list.");
				} else {

					if(!UUIDUtils.isPremium(args[0])) {
						PluginUtils.send(p, "�cThis player is not premium.");
						return;
					}

					ConfigUtils.enablePremium(args[0]);
					ConfigUtils.player_save();
					ConfigUtils.player_reload();

					PluginUtils.send(p, "�aSuccessfully enabled PremiumLogin to " + args[0]);

					try {
						ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
						if(target.isConnected()) {
							target.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("enabled-autologin"))));
						}
					} catch(Exception exc) {
						PluginUtils.send(p, "�aSuccessfully enabled PremiumLogin to " + args[0]);
					}
					return;
				}
			} catch(Exception exc) {
				sender.sendMessage(new TextComponent("�cUnable to do this action."));
			}
			return;
		}

		ProxiedPlayer p = (ProxiedPlayer) sender;

		try {

			if(!p.hasPermission("premiumlogin.staff")) {
				PluginUtils.sendParseColors(p, ConfigUtils.getConfStr("no-perms"));
				return;
			}

			if(args.length != 1) {
				PluginUtils.send(p, "�7Correct Usage: �f/premiumadd (player) �7- �bAdd a player to premium players list.");
				return;
			}


			if(ConfigUtils.hasPremiumAutoLogin(args[0])) {
				PluginUtils.send(p, "�cThis player is arleady in premium list.");
			} else {

				if(!UUIDUtils.isPremium(args[0])) {
					PluginUtils.send(p, "�cThis player is not premium.");
					return;
				}

				ConfigUtils.enablePremium(args[0]);
				ConfigUtils.player_save();
				ConfigUtils.player_reload();

				PluginUtils.send(p, "�aSuccessfully enabled PremiumLogin to " + args[0]);
				try {
					ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
					if(target.isConnected()) {
						target.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("enabled-autologin"))));
					}
				} catch(Exception exc) {
					PluginUtils.send(p, "�aSuccessfully enabled PremiumLogin to " + args[0]);
				}
				return;
			}
		} catch(Exception exc) {
			PluginUtils.send(p, "�cUnable to do this action.");
		}
}
}

