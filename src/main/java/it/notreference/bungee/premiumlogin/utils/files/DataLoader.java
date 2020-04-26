package it.notreference.bungee.premiumlogin.utils.files;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;


/**
 *
 * PremiumLogin 1.7 By NotReference
 *
 * @author NotReference
 * @version 1.7
 * @destination BungeeCord
 *
 */

public class DataLoader {

    private Configuration dataFile;
    private File file;
    private PremiumLoginFilesUtils utils;

    public DataLoader(PremiumLoginFilesUtils utils, File dataFile) {
      try {

    this.dataFile = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dataFile);
    this.file = dataFile;
    this.utils = utils;

      } catch(Exception exc) {
          throw new RuntimeException("Could not load player data file.");
      }
    }

    /**
     *
     * Returns the data file.
     *
     * @return
     */
    public Configuration getData() {
        return dataFile;
    }

    /**
     *
     * Saves the data file.
     *
     * @throws Exception
     */
    public void saveData() throws Exception {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(dataFile, file);
    }

    /**
     *
     * Reloads the data.
     *
     * @throws Exception
     */
    public void reloadData() throws Exception {
        this.dataFile = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }


}
