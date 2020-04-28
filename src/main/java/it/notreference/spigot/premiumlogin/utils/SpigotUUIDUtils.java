package it.notreference.spigot.premiumlogin.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URL;
import java.util.Scanner;
import java.util.UUID;


/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination Spigot
 *
 */
public class SpigotUUIDUtils {

    /**
     *
     * Checks if is Premium.
     *
     * @param playerName
     * @return
     */
    public static boolean isPremium(String playerName) {
        return  !getPremiumUUID(playerName).equalsIgnoreCase("UNABLE_O_NO_PREMIUM");
    }

    /**
     *
     * Returns the cracked uuid of the player.
     *
     * @param name
     * @return
     */
    public static String getCrackedUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()).toString();
    }

    /**
     *
     * Returns the premium uuid of the player.
     *
     * @param name
     * @return
     */
    public static String getPremiumUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            Scanner scanner = new Scanner(url.openStream());
            String line = scanner.nextLine();
            scanner.close();
            JsonObject obj = new Gson().fromJson(line, JsonObject.class);
            return obj.get("id").getAsString();
        } catch (Exception ex) {
            return "UNABLE_O_NO_PREMIUM";
        }
    }


}
