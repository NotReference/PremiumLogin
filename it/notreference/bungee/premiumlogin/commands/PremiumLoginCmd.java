package it.notreference.bungee.premiumlogin.commands;

import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * PremiumLogin 1.1 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PremiumLoginCmd extends Command{

	public PremiumLoginCmd() {
		super("premiumlogin");
	}

	private static String by = "§7This server is using §bPremiumLogin 1.1 §7by §eNotReference§7.";
	
	public static String getByMessage() {
		return by;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		Messages.send(p, by);
		Messages.send(p, "§7Your informations:");
		if(UUIDUtils.isPremium(p)) {
		Messages.send(p, "§7Premium:§a Yes");
		} else {
			Messages.send(p, "§7Premium:§c No");
		}
		if(UUIDUtils.isPremiumConnection(p)) {
			Messages.send(p, "§7Using Premium Launcher:§a Yes");
		} else {
			Messages.send(p, "§7Using Premium Launcher:§c No");
		}
		if(ConfigUtils.hasPremiumAutoLogin(p)) {
			Messages.send(p, "§7Default login system: §aPremiumLogin");
		} else {
			Messages.send(p, "§7Default login system: §eAuthMe");
		}
		
	}
	
}
