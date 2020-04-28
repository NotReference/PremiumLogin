package it.notreference.spigot.premiumlogin;

import it.notreference.spigot.premiumlogin.utils.PLSpigotFiles;
import org.bukkit.Bukkit;
import it.notreference.spigot.premiumlogin.utils.Messages;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class PLComandi implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("premium")) {

            if(!(commandSender instanceof  Player)) {

                commandSender.sendMessage("§cYou must be a player to perform this command.");
                return false;

            }

            Player p = (Player) commandSender;
            YamlConfiguration config = PremiumLoginSpigot.get().getConfig();
            PLSpigotFiles utils = PremiumLoginSpigot.get().getConfigManager();
            YamlConfiguration dataFile = PremiumLoginSpigot.get().getDataFile();
            if(!config.getString("misc.premium-permission").equalsIgnoreCase("null")) {

                String permesso = config.getString("misc.premium-permission");

                if(p.hasPermission(permesso)) {

                    if(utils.hasPremiumLogin(p.getName())) {

                        utils.togglePremium(p.getName(), false);
                        p.kickPlayer(Messages.parse(config.getString("messages.disable-autologin")));
                        return false;

                    } else {

                        utils.togglePremium(p.getName(), true);
                        p.kickPlayer(Messages.parse(config.getString("messages.enabled-autologin")));
                        return false;

                    }

                } else {

                    p.sendMessage(Messages.parse(config.getString("messages.no-perms")));
                    return false;

                }

            } else {



                if(utils.hasPremiumLogin(p.getName())) {

                    utils.togglePremium(p.getName(), false);
                    p.kickPlayer(Messages.parse(config.getString("messages.disable-autologin")));
                    return false;

                } else {

                    utils.togglePremium(p.getName(), true);
                    p.kickPlayer(Messages.parse(config.getString("messages.enabled-autologin")));
                    return false;

                }

            }
        }

        if(cmd.getName().equalsIgnoreCase("premiumlist")) {
                Bukkit.getServer().dispatchCommand(commandSender,  "mcauth getpremiumlist");
                return false;
        }

        if(cmd.getName().equalsIgnoreCase("premiumlogin")) {
            commandSender.sendMessage("§7This server is using §ePremiumLoginSpigot §7by §bNotReference§7.");
            return false;
        }

        if(cmd.getName().equalsIgnoreCase("premiumreload")) {

            if(!(commandSender instanceof Player)) {

                PremiumLoginSpigot.get().reloadConfig();
                try {
                    PremiumLoginSpigot.get().getDataFile().load(new File("playersdata.yml"));
                } catch(Exception exc) {
                    commandSender.sendMessage("§cUnable to reload the config file.");
                    return false;
                }
                commandSender.sendMessage("§aDone.");
                return false;
            }

            commandSender.sendMessage("§7This server is using §ePremiumLoginSpigot §7by §bNotReference§7.");
            return false;
        }

        return false;
    }
}
