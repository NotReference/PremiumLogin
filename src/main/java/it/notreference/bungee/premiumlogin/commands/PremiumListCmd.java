package it.notreference.bungee.premiumlogin.commands;


import it.notreference.bungee.premiumlogin.PremiumLoginMain;
import it.notreference.bungee.premiumlogin.api.PremiumOnlineConnection;
import it.notreference.bungee.premiumlogin.utils.ConfigUtils;
import it.notreference.bungee.premiumlogin.utils.PluginUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

public class PremiumListCmd extends Command {

    public PremiumListCmd() { super("premiumlist"); }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        PluginUtils.send(commandSender, "&ePlease wait.. The task is running asynchronous to prevent crash (if many players).. This can take a while.");
        PremiumLoginMain.i().getProxy().getScheduler().runAsync(PremiumLoginMain.i(), () -> {


            if (!(commandSender instanceof ProxiedPlayer)) {

                try {

                    List<String> names = new ArrayList<String>();
                    List<PremiumOnlineConnection> connections = PremiumLoginMain.i().getPremiumConnections();
                    for (PremiumOnlineConnection con : connections) {
                      names.add(con.getPlayerName());
                    }
                    PluginUtils.send(commandSender, "&7Autologged in premium players list: &e" + names);

                } catch (Exception exc) {
                    PluginUtils.send(commandSender, "&cUnable to get local players sessions list.");
                    return;
                }


            } else {

                ProxiedPlayer p = (ProxiedPlayer) commandSender;
                if (!p.hasPermission("premiumlogin.list")) {
                    PluginUtils.send(p, ConfigUtils.getConfStr("no-perms"));
                    return;
                } else {

                    try {
                        List<String> names = new ArrayList<String>();
                        List<PremiumOnlineConnection> connections = PremiumLoginMain.i().getPremiumConnections();
                        for (PremiumOnlineConnection con : connections) {
                            names.add(con.getPlayerName());
                        }
                        PluginUtils.send(commandSender, "&7Autologged in premium players list: &e" + names);
                    } catch(Exception exc) {
                        PluginUtils.send(commandSender, "&cUnable to get local players sessions list.");
                        return;
                    }

                }

            }
        });
    }

}
