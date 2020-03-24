package it.notreference.bungee.premiumlogin.commands;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.Messages;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * PremiumLogin 1.3 by NotReference
 *
 * @description Autologin premium players easily and safely.
 * @dependency AuthMe 5.5.0
 */

public class PremiumLookUpCmd extends Command{

	public PremiumLookUpCmd() {
		super("premiumlookup");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			CommandSender p = sender;
			if(args.length == 1) {
				try {
				
				ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
				
				if(!target.isConnected() || target == null) {
					Messages.send(p, "§b" + args[0] + " §7is §coffline.");
					Messages.send(p, "§7Offline Info:");
					if(!ConfigUtils.hasPremiumAutoLogin(target)) {
					Messages.send(p, "§7Default Login System:§b AuthMe");
					} else {
						Messages.send(p, "§c§l(!) §7Default Login System:§b PremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Premium:§a Yes");
					} else {
						Messages.send(p, "§7Premium: §cNo");
					}
					if(!UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Recommended Login System:§e AuthMe");
					} else {
						Messages.send(p, "§7Recommended Login System: §ePremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
					} else {
						Messages.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
					}
				} else {
					Messages.send(p, "§b" + args[0] + " §7è §aonline§7 su§b " + target.getServer().getInfo().getName() + " §7.");
					Messages.send(p, "§7Info:");
					if(ConfigUtils.hasPremiumAutoLogin(target)) {
						Messages.send(p, "§7Logged in with:§a PremiumLogin");
						Messages.send(p, "§7Used Launcher:§a Minecraft ");
					} else {
						Messages.send(p, "§7Logged in with:§b AuthMe");
					}
					if(!ConfigUtils.hasPremiumAutoLogin(target)) {
					Messages.send(p, "§7Default Login System:§b AuthMe");
					} else {
						Messages.send(p, "§7Default Login System:§b PremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Premium:§a Yes");
					} else {
						Messages.send(p, "§7Premium: §cNo");
					}
					if(!UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Recommended Login System:§e AuthMe");
					} else {
						Messages.send(p, "§7Recommended Login System: §ePremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
					} else {
						Messages.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
					}
				}
				} catch(Exception exc) {
					Messages.send(p, "§cUnable to get player informations. Is this player offline?");
				}
			} else {
				Messages.send(p, "§7Correct Usage:§f /premiumlookup (player)");
			}
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(p.hasPermission("premiumlogin.staff")) {
			if(args.length == 1) {
				try {
				ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
				if(!target.isConnected() || target == null) {
					Messages.send(p, "§b" + args[0] + " §7è §coffline.");
					Messages.send(p, "§7Offline Info:");
					if(!ConfigUtils.hasPremiumAutoLogin(target)) {
					Messages.send(p, "§7Default Login System:§b AuthMe");
					} else {
						Messages.send(p, "§c§l(!) §7Default Login System:§b PremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Premium:§a Yes");
					} else {
						Messages.send(p, "§7Premium: §cNo");
					}
					if(!UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Recommended Login System:§e AuthMe");
					} else {
						Messages.send(p, "§7Recommended Login System: §ePremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
					} else {
						Messages.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
					}
				} else {
					Messages.send(p, "§b" + args[0] + " §7è §aonline§7 su§b " + target.getServer().getInfo().getName() + " §7.");
					Messages.send(p, "§7Info:");
					if(ConfigUtils.hasPremiumAutoLogin(target)) {
						Messages.send(p, "§7Logged in with:§a PremiumLogin");
						Messages.send(p, "§7Used Launcher:§a Minecraft ");
					} else {
						Messages.send(p, "§7Logged in with:§b AuthMe");
					}
					if(!ConfigUtils.hasPremiumAutoLogin(target)) {
					Messages.send(p, "§7Default Login System:§b AuthMe");
					} else {
						Messages.send(p, "§7Default Login System:§b PremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Premium:§a Yes");
					} else {
						Messages.send(p, "§7Premium: §cNo");
					}
					if(!UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7Recommended Login System:§e AuthMe");
					} else {
						Messages.send(p, "§7Recommended Login System: §ePremiumLogin");
					}
					if(UUIDUtils.isPremium(target)) {
					Messages.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
					} else {
						Messages.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
					}
				}
				} catch(Exception exc) {
					Messages.send(p, "§cUnable to get player informations. Is this player offline?");
				}
			} else {
				Messages.send(p, "§7Correct Usage:§f /premiumlookup (player)");
			}
		} else {
			Messages.send(p, ConfigUtils.getConfStr("no-perms"));
		}
		
	}
	
}
