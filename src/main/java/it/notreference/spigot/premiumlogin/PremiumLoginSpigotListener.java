package it.notreference.spigot.premiumlogin;

import it.notreference.minecraftauth.events.MinecraftOnlineModeSetEvent;
import it.notreference.minecraftauth.events.MinecraftPlayerJoinEvent;
import it.notreference.spigot.premiumlogin.auth.SpigotAuthenticationHandler;
import it.notreference.spigot.premiumlogin.auth.SpigotKey;
import it.notreference.spigot.premiumlogin.auth.misc.SpigotKeyBuilder;
import it.notreference.spigot.premiumlogin.utils.Messages;
import it.notreference.spigot.premiumlogin.utils.PLSpigotFiles;
import it.notreference.spigot.premiumlogin.utils.SpigotUUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitWorker;

/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination Spigot
 *
 */
public class PremiumLoginSpigotListener implements Listener {

    @EventHandler
    public void onOnline(MinecraftOnlineModeSetEvent event) {

        String playerName = event.getPlayerName();
        PLSpigotFiles configManager = PremiumLoginSpigot.get().getConfigManager();
        PremiumLoginSpigot main = PremiumLoginSpigot.get();
        main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> {
            Player player = main.getServer().getPlayer(playerName);
            if (player == null) {
                main.warn("Received an online mode session start event but the player is null. (" + playerName + ")");
                return;
            }

            if (!configManager.hasPremiumLogin(playerName)) {
                return;
            }

            SpigotAuthenticationHandler handler = (SpigotAuthenticationHandler) PremiumLoginSpigot.get().getAuthHandler();
            SpigotKey key = new SpigotKeyBuilder()
                    .setAuthPlugin(configManager.defaultAuthPlugin())
                    .setPlayer(player)
                    .setPlayerName(playerName)
                    .setVerifyToken(new byte[4])
                    .build();
            handler.premiumLogin(player, key);
        }, 17L);


    }

    @EventHandler
    public void onJoin(MinecraftPlayerJoinEvent event) {

        if(event.isCancelled()) {
            return;
        }

        event.preventMainEvents(PremiumLoginSpigot.get());

        String playerName = event.getPlayerName();
        PLSpigotFiles configManager = PremiumLoginSpigot.get().getConfigManager();
        PremiumLoginSpigot main = PremiumLoginSpigot.get();

        if(main.getConfig().getBoolean("auto-register-premium-players")) {

            if(!SpigotUUIDUtils.isPremium(playerName)) {

                configManager.togglePremium(playerName, false);
                event.getRequest().allowSPConnection();
                return;

            } else {

                try {
                    event.setOnlineMode();
                } catch(Exception exc) {
                    main.error("" + exc);
                    main.error("An error has occurred while handling request.");
                    try {
                        event.getRequest().kickPlayer("§cUnable to authenticate you with Minecraft.net: Failed to set online mode. Please rejoin.");
                    } catch(Exception excee) {

                    }
                    event.setCancelled(true);
                    return;
                }


            }
            return;
        }

        if(configManager.hasPremiumLogin(playerName)) {

            if (!SpigotUUIDUtils.isPremium(playerName)) {

                configManager.togglePremium(playerName, false);
                event.setCancelMessage("§cUnable to authenticate you with Minecraft.net: You are not premium. \r\n §eYour profile data has been refreshed.  Please rejoin.");
                event.setCancelled(true);
                return;

            }
            try {
                event.setOnlineMode();
            } catch (Exception exc) {
                main.error("" + exc);
                main.error("An error has occurred while handling request.");
                try {
                    event.getRequest().kickPlayer("§cUnable to authenticate you with Minecraft.net: Failed to set online mode. Please rejoin.");
                } catch(Exception excee) {

                }
                event.setCancelled(true);
                if (main.getConfig().getBoolean("log-staff")) {


                    for (Player p : main.getServer().getOnlinePlayers()) {

                        if (p.hasPermission("premiumlogin.log")) {
                            p.sendMessage(Messages.parse(main.getConfig().getString("staff-log.user-fail")));
                        }

                    }

                }
                return;
            }
        } else {

            event.getRequest().allowSPConnection();

        }

    }

    public int getTaskList() {
            int workers = 0;
            for (BukkitWorker worker : Bukkit.getScheduler().getActiveWorkers()) {
                if (worker.getOwner().equals(this)) {
                    workers++;
                }
            }
            return workers;
    }

}
