package it.notreference.bungee.premiumlogin.commands;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
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

public class PremiumLoginCmd extends Command{

	public PremiumLoginCmd() {
		super("premiumlogin");
	}

	private static String by = "§7This server is using §bPremiumLogin " + PremiumLoginMain.i().currentVersion() + " §7by §eNotReference§7.";
	
	public static String getByMessage() {
		return by;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(by));
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		Messages.send(p, by);
		
		if(ConfigUtils.getConfBool("disable-informations")) {
			return;
		}
		
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
