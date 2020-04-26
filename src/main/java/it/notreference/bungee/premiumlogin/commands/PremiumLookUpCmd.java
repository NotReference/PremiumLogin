package it.notreference.bungee.premiumlogin.commands;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.PluginUtils;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;



/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
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
							PluginUtils.send(p, "§b" + args[0] + " §7is §coffline.");
							PluginUtils.send(p, "§7Offline Info:");
							if(!ConfigUtils.hasPremiumAutoLogin(target)) {
								PluginUtils.send(p, "§7Default Login System:§b AuthMe");
							} else {
								PluginUtils.send(p, "§7Default Login System:§b PremiumLogin");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Premium:§a Yes");
							} else {
								PluginUtils.send(p, "§7Premium: §cNo");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Recommended Login System:§e PremiumLogin");
							} else {
								PluginUtils.send(p, "§7Recommended Login System: §eAuthMe");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
							} else {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
							}
						} else {
							PluginUtils.send(p, "§b" + args[0] + " §7è §aonline§7 su§b " + target.getServer().getInfo().getName() + " §7.");
							PluginUtils.send(p, "§7Info:");
							if(ConfigUtils.hasPremiumAutoLogin(target)) {
								PluginUtils.send(p, "§7Logged in with:§a PremiumLogin");
								PluginUtils.send(p, "§7Used Launcher:§a Minecraft ");
							} else {
								PluginUtils.send(p, "§7Logged in with:§b AuthMe");
							}
							if(ConfigUtils.hasPremiumAutoLogin(target)) {
								PluginUtils.send(p, "§7Default Login System:§b PremiumLogin");
							} else {
								PluginUtils.send(p, "§7Default Login System:§b AuthMe");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Premium:§a Yes");
							} else {
								PluginUtils.send(p, "§7Premium: §cNo");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Recommended Login System:§e PremiumLogin");
							} else {
								PluginUtils.send(p, "§7Recommended Login System: §eAuthMe");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
							} else {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
							}
						}
					} catch(Exception exc) {
						PluginUtils.send(p, "§cUnable to get player informations. Is this player offline?");
					}
				} else {
					PluginUtils.send(p, "§7Correct Usage:§f /premiumlookup (player)");
				}
				return;
			}

			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(p.hasPermission("premiumlogin.staff")) {
				if(args.length == 1) {
					try {
						ProxiedPlayer target = PremiumLoginMain.i().getProxy().getPlayer(args[0]);
						if(!target.isConnected() || target == null) {
							PluginUtils.send(p, "§b" + args[0] + " §7è §coffline.");
							PluginUtils.send(p, "§7Offline Info:");
							if(!ConfigUtils.hasPremiumAutoLogin(target)) {
								PluginUtils.send(p, "§7Default Login System:§b AuthMe");
							} else {
								PluginUtils.send(p, "§c§l(!) §7Default Login System:§b PremiumLogin");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Premium:§a Yes");
							} else {
								PluginUtils.send(p, "§7Premium: §cNo");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Recommended Login System:§e PremiumLogin");
							} else {
								PluginUtils.send(p, "§7Recommended Login System: §eAuthMe");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
							} else {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
							}
						} else {
							PluginUtils.send(p, "§b" + args[0] + " §7è §aonline§7 su§b " + target.getServer().getInfo().getName() + " §7.");
							PluginUtils.send(p, "§7Info:");
							if(ConfigUtils.hasPremiumAutoLogin(target)) {
								PluginUtils.send(p, "§7Logged in with:§a PremiumLogin");
								PluginUtils.send(p, "§7Used Launcher:§a Minecraft ");
							} else {
								PluginUtils.send(p, "§7Logged in with:§b AuthMe");
							}
							if(!ConfigUtils.hasPremiumAutoLogin(target)) {
								PluginUtils.send(p, "§7Default Login System:§b AuthMe");
							} else {
								PluginUtils.send(p, "§7Default Login System:§b PremiumLogin");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Premium:§a Yes");
							} else {
								PluginUtils.send(p, "§7Premium: §cNo");
							}
							if(!UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7Recommended Login System:§e AuthMe");
							} else {
								PluginUtils.send(p, "§7Recommended Login System: §ePremiumLogin");
							}
							if(UUIDUtils.isPremium(target)) {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getPremiumUUID(target.getName()));
							} else {
								PluginUtils.send(p, "§7UUID:§f " + UUIDUtils.getCrackedUUID(target.getName()));
							}
						}
					} catch(Exception exc) {
						PluginUtils.send(p, "§cUnable to get player informations. Is this player offline?");
					}
				} else {
					PluginUtils.send(p, "§7Correct Usage:§f /premiumlookup (player)");
				}
			} else {
				PluginUtils.send(p, ConfigUtils.getConfStr("no-perms"));
			}

		}
	
}
