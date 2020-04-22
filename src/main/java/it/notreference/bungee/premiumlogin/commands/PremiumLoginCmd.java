package it.notreference.bungee.premiumlogin.commands;

import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.PluginUtils;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


/**
 *
 * PremiumLogin 1.6.5 By NotReference
 *
 * @author NotReference
 * @version 1.6.5
 * @destination BungeeCord
 *
 */

public class PremiumLoginCmd extends Command{

	public PremiumLoginCmd() {
		super("premiumlogin");
	}

	private static String by = "§7This server is using §bPremiumLogin 1.6.4 §7by §eNotReference§7.";

	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(by));
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		PluginUtils.send(p, by);
		
		if(ConfigUtils.getConfBool("disable-informations")) {
			return;
		}
		
		PluginUtils.send(p, "§7Your informations:");
		if(UUIDUtils.isPremium(p)) {
		PluginUtils.send(p, "§7Premium:§a Yes");
		} else {
			PluginUtils.send(p, "§7Premium:§c No");
		}
		if(UUIDUtils.isPremiumConnection(p)) {
			PluginUtils.send(p, "§7Using Premium Launcher:§a Yes");
		} else {
			PluginUtils.send(p, "§7Using Premium Launcher:§c No");
		}
		if(ConfigUtils.hasPremiumAutoLogin(p)) {
			PluginUtils.send(p, "§7Default login system: §aPremiumLogin");
		} else {
			PluginUtils.send(p, "§7Default login system: §eAuthMe");
		}
		
	}
	
}
