package it.notreference.bungee.premiumlogin;

import java.io.*;
import java.nio.file.Files;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * PremiumLogin 1.6.4 By NotReference
 *
 * @author NotReference
 * @version 1.6.4
 * @destination BungeeCord
 *
 */

public class PLConfiguration {

    private PremiumLoginMain main;
    private File last;

    /**
     *
     * PLConfiguration b1.0 by NotReference
     *
     * @author NotReference
     * @for PremiumLogin
     * @param main the plugin main
     */
    public PLConfiguration(PremiumLoginMain main) {
        this.main = main;
    }

    /**
     *
     * Deletes a file
     *
     * @return status
     */
    public boolean delete() {
        return last.delete();
    }

    /**
     *
     * Replaces the last temp file data with new file data.
     *
     * @param fileName the filename ex config.yml
     * @return false for fail, true for success
     */
    public boolean replaceData(String fileName) {
        if(last != null) {
            try (InputStream fileStream = getClass().getResourceAsStream(fileName)) {

                String randomName = main.randomString( 6);
                File file = new File(last.getAbsolutePath());
                Files.copy(fileStream, file.toPath());
                fileStream.close();
                return true;

            } catch(Exception exc) {
                exc.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *
     * Returns the input stream of the file.
     *
     * @param f
     * @return
     * @throws Exception
     * @throws FileNotFoundException
     */
    public InputStream inputStreamFromFile(File f) throws Exception, FileNotFoundException {
     return new FileInputStream(f);
    }

    /**
     *
     * Deletes all temp files except config & players files.
     *
     * @return status code
     */

    /*

    1.6.4: Little issue fix.

     */
    public boolean deleteAllTempFiles() {
        try {
            File dir = main.getDataFolder();

            //Prevent from deleting files if dir doesn't contains PremiumLogin
            if (!dir.getAbsolutePath().toLowerCase().contains("PremiumLogin".toLowerCase())) {
                return false;
            }

            List<Boolean> v = new ArrayList<Boolean>();
            for (File file : dir.listFiles()) {
                try {
                    if (file.getName().equalsIgnoreCase("config.yml") || file.getName().equalsIgnoreCase("players.yml")) {
                        //non eliminiamo
                    } else {
                        v.add(file.delete());
                    }
                } catch (Exception exc) {
                    v.add(false);
                }
            }
            for (int e = 0; e < v.size() - 1; e++) {
                if (!v.get(e)) {
                    return false;
                }
            }
            return true;
        } catch(Exception exc) {
            main.getLogger().severe("ERROR - Unable to delete all temp files. (If you are using linux, start the server using sudo) Please delete them manually.");
            return false;
        }
    }

    /**
     *
     * Converts a string to an input stream.
     *
     * @param e the string
     * @return
     */
    public InputStream convertStr(String e) {
        return new ByteArrayInputStream(e.getBytes());
    }

    /**
     *
     * Replaces the last temp file data with new custom data.
     *
     * @param stringBuilderData new data
     * @return false for fail, true for success
     */
    public boolean writeData(StringBuilder stringBuilderData) {
        if(last != null) {
            try (InputStream dataStream = convertStr(stringBuilderData.toString())) {

                String randomName = main.randomString( 6);
                File file = new File(last.getAbsolutePath());
                Files.copy(dataStream, file.toPath());
                dataStream.close();
                return true;

            } catch(Exception exc) {
                exc.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *
     * Copies the config.yml to another temp file.
     *
     * @why I use this for prevent that the file config.yml lose comments ( # )
     * @return the new temp file
     */
    public File copyConfiguration() {

        try (InputStream fileStream = inputStreamFromFile(new File(main.getDataFolder(), "config.yml"))) {

            String randomName = main.randomString( 6).replace("\\", "e").replace("/", "Y");
            File file = new File(main.getDataFolder(), randomName + ".yml");
            Files.copy(fileStream, file.toPath());
            fileStream.close();
            last = file;
            return new File(main.getDataFolder(), randomName + ".yml");

        } catch(Exception exc) {
            exc.printStackTrace();
            return null;
        }

    }

    /**
     *
     * Returns the last created temp file
     *
     * @return temp file
     */
    public File getLastTempFile() {
        return last;
    }


}
