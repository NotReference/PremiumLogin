package it.notreference.bungee.premiumlogin.commands;

import it.notreference.bungee.premiumlogin.PremiumLoginEventManager;
import it.notreference.bungee.premiumlogin.api.SwitchType;
import it.notreference.bungee.premiumlogin.api.events.PremiumSwitchEvent;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.PluginUtils;
import it.notreference.bungee.premiumlogin.utils.UUIDUtils;
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


public class PremiumCmd extends Command {

	public PremiumCmd() {
		super("premium");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		try {
			if (!(sender instanceof ProxiedPlayer)) {
				sender.sendMessage(new TextComponent("�cERROR - You must be a player."));
				return;
			}

			ProxiedPlayer p = (ProxiedPlayer) sender;
			if (ConfigUtils.permessoSettato()) {
				if (!p.hasPermission(ConfigUtils.getPremiumPerm())) {
					PluginUtils.sendParseColors(p, ConfigUtils.getConfStr("no-perms"));
					return;
				}
			}

			if (!UUIDUtils.isPremium(p.getName())) {
				PluginUtils.send(p, ConfigUtils.getConfStr("no-premium"));
				return;
			}

			if (ConfigUtils.hasPremiumAutoLogin(p.getName())) {

				try {
					PremiumLoginEventManager.fire(new PremiumSwitchEvent(p, p.getServer().getInfo(), SwitchType.PREMIUMLOGINDISABLED));
				} catch (Exception e1) {

				}
				try {
					ConfigUtils.disablePremium(p.getName());
					ConfigUtils.player_save();
				} catch (Exception exc) {
					p.sendMessage(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("error-generic"))));
					return;
				}
				PluginUtils.send(p, ConfigUtils.getConfStr("disable-autologin"));
				PluginUtils.send(p, ConfigUtils.getConfStr("default-login-system-switch-to-authme"));
				p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("disable-autologin"))));
			} else {
				try {
					PremiumLoginEventManager.fire(new PremiumSwitchEvent(p, p.getServer().getInfo(), SwitchType.PREMIUMLOGINENABLED));
				} catch (Exception e1) {

				}
				ConfigUtils.enablePremium(p.getName());
				ConfigUtils.player_save();
				ConfigUtils.player_reload();
				PluginUtils.send(p, PluginUtils.parse(ConfigUtils.getConfStr("enabled-autologin")));
				p.disconnect(new TextComponent(PluginUtils.parse(ConfigUtils.getConfStr("enabled-autologin"))));
			}
		} catch (Exception exc) {
       sender.sendMessage("�cAn error has occurred while performing this command.");
		}
	}
}

