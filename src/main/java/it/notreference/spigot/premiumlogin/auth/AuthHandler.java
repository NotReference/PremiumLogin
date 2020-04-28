package it.notreference.spigot.premiumlogin.auth;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.karmaconfigs.Spigot.API.PlayerAPI;
import it.notreference.minecraftauth.auth.MinecraftEncryptionUtils;
import it.notreference.spigot.premiumlogin.PremiumLoginSpigot;
import it.notreference.spigot.premiumlogin.utils.Messages;
import it.notreference.spigot.premiumlogin.utils.PLSpigotFiles;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination Spigot
 *
 */

public class AuthHandler implements SpigotAuthenticationHandler {

   private final PremiumLoginSpigot main;

    public AuthHandler(PremiumLoginSpigot main) {
        this.main = main;
    }


    @Override
    public void premiumLogin(Player spigotPlayer, SpigotKey key) {

     AuthPlugin plugin = key.getAuthPlugin();
     String playerName = key.getPlayerName();

     if(spigotPlayer == null) {
      error("Received an unexpected login request from: " + key.getPlayerName());
      return;
     }

     info("Received a login request from: " + key.getPlayerName());
     info("Authenticating " + playerName + "...");


      if(plugin == AuthPlugin.LOCKLOGIN) {

       PlayerAPI lockLoginAPI = new PlayerAPI(spigotPlayer);
       if(!lockLoginAPI.isRegistered()) {

        String pass = generateRandomSecurePassword();
        lockLoginAPI.register(pass);
        lockLoginAPI.setLogged(true);
        spigotPlayer.sendMessage(Messages.parse(main.getConfig().getString("messages.auto-register")).replace("{passw}", pass));
        spigotPlayer.sendMessage(Messages.parse(main.getConfig().getString("messages.change-auto-register")).replace("{passw}", pass));
        info("Successfully premium authenticated (auto-registred) user " + playerName + " with LockLoginApi.");
        if(main.getConfig().getBoolean("log-staff")) {


         for(Player p: main.getServer().getOnlinePlayers()) {

          if(p.hasPermission("premiumlogin.log")) {
           p.sendMessage(Messages.parse(main.getConfig().getString("staff-log.user-login")));
          }

         }

        }

       } else {

        lockLoginAPI.setLogged(true);
        spigotPlayer.sendMessage(Messages.parse(main.getConfig().getString("messages.auto-login-premium")));
        info("Successfully premium authenticated user " + playerName + " with LockLoginApi.");

       }

      } else {

       AuthMeApi authMeApi = AuthMeApi.getInstance();
       if(!authMeApi.isRegistered(playerName)) {

        String pass = generateRandomSecurePassword();
        authMeApi.registerPlayer(playerName, pass);
        authMeApi.forceLogin(spigotPlayer);
        spigotPlayer.sendMessage(Messages.parse(main.getConfig().getString("messages.auto-register")).replace("{passw}", pass));
        spigotPlayer.sendMessage(Messages.parse(main.getConfig().getString("messages.change-auto-register")).replace("{passw}", pass));
        info("Successfully premium authenticated (auto-registred) user " + playerName + " with AuthMeApi.");
        if(main.getConfig().getBoolean("log-staff")) {


         for (Player p : main.getServer().getOnlinePlayers()) {

          if (p.hasPermission("premiumlogin.log")) {
           p.sendMessage(Messages.parse(main.getConfig().getString("messages.staff-log.user-login")));
          }

         }

        }

       } else {

        authMeApi.forceLogin(spigotPlayer);
        spigotPlayer.sendMessage(Messages.parse(main.getConfig().getString("messages.auto-login-premium")));
        info("Successfully premium authenticated user " + playerName + " with AuthMeApi.");
        if(main.getConfig().getBoolean("log-staff")) {


         for(Player p: main.getServer().getOnlinePlayers()) {

          if(p.hasPermission("premiumlogin.log")) {
           p.sendMessage(Messages.parse(main.getConfig().getString("staff-log.user-login")));
          }

         }

        }

       }


      }
    }

    @Override
    public void info(String msg) {
    main.info(msg);
    }

    @Override
    public void error(String msg) {
   main.error(msg);
    }

    @Override
    public AuthPlugin getDefaultAuthPlugin() {
        return main.getConfigManager().defaultAuthPlugin();
    }

 /**
  *
  * Encodes a string in base64.
  *
  * @param d
  * @return
  */
 public String base64_encode(String d) {
     return Base64.getEncoder().encodeToString(d.getBytes());
    }

    @Override
    public String generateRandomSecurePassword() {

     byte[] bytes = new byte[6];
     Random random = new Random();
     random.nextBytes(bytes);
     int numbers = random.nextInt(4);
     byte[] last = new byte[4];
     String s = "p17";
     String p1 = base64_encode(new String(bytes) + numbers + new String(last) + s);
     return p1;

    }


}
