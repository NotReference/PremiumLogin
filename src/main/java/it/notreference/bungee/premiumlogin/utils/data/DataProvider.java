package it.notreference.bungee.premiumlogin.utils.data;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;


/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

public class DataProvider {


    /**
     *
     * Returns a new data handler
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> clazz) {

        return (T) PremiumLoginMain.i().newDataHandler(clazz);

    }

}
