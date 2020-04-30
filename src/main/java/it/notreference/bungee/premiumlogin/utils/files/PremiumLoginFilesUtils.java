package it.notreference.bungee.premiumlogin.utils.files;

import it.notreference.bungee.premiumlogin.PremiumLoginMain;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




/**
 *
 * PremiumLogin 1.7.1 By NotReference
 *
 * @author NotReference
 * @version 1.7.1
 * @destination BungeeCord
 *
 */


public class PremiumLoginFilesUtils {

    private PremiumLoginMain main;
    private File last;

    /**
     *
     * PremiumLoginFilesUtils 1.3 by NotReference
     *
     * @author NotReference
     * @for PremiumLogin
     * @param main the plugin main
     */
    public PremiumLoginFilesUtils(PremiumLoginMain main) {
        this.main = main;
    }

    /**
     *
     * Deletes the temp file.
     *
     * @return status
     */
    public boolean delete() {
        return last.delete();
    }

    /**
     *
     * Check if is Windows.
     *
     * @param OS
     * @return
     */

    public boolean isWin(String OS) {
        return OS.toLowerCase().contains("win");
    }

    /**
     *
     * Check if is MAC.
     *
     * @param OS
     * @return
     */

    public static boolean isMac(String OS) {
        return OS.toLowerCase().contains("mac");
    }

    /**
     *
     * Check if is linux.
     *
     * @param OS
     * @return
     */
    public static boolean isLinux(String OS) {
        OS = OS.toLowerCase();
        return (OS.contains("nux") || OS.contains("nix") || OS.contains("aix") || OS.contains("sunos") || OS.contains("ubu"));
    }

    /**
     *
     * Returns the current OS.
     *
     * @return
     */
    public OS getCurrentOs() {
        try {

            String nome = System.getProperty("os.name");
            main.getLogger().info("INFO - Your OS: " + nome);
            if(isWin(nome)) {
                return OS.WIN;
            } else if(isMac(nome)) {
                return OS.MAC;
            } else if(isLinux(nome)) {
                return OS.LINUX;
            } else {
                return OS.UNKNOWN;
            }
        } catch(Exception exc) {
            main.getLogger().warning("Unable to detect your OS. The temp file can be damaged.");
            return null;
        }
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
                    if (file.getName().equalsIgnoreCase("config.yml") || file.getName().equalsIgnoreCase("players.yml") || file.getName().equalsIgnoreCase("codes.yml")) {
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
     * Reads the file content.
     *
     * @param f
     * @return
     * @throws Exception
     */
    public String readContent(File f) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10];
        while (reader.read(buffer) != -1) {
            stringBuilder.append(new String(buffer));
            buffer = new char[10];
        }
        reader.close();

        return stringBuilder.toString();
    }

    /**
     *
     * Returns a String from InputStream
     *
     * @param in
     * @return
     */
    public String inputStreamFromString(InputStream in) {
        Scanner scanner = new Scanner(in);
        StringBuffer buf = new StringBuffer();
        while(scanner.hasNext()){
            buf.append(scanner.nextLine());
        }
        return buf.toString();
    }

    /**
     *
     * Writes a file for Linux Kernel.
     *
     * @param input
     * @param to
     * @return
     * @throws IOException
     */
    public boolean linuxWrite(InputStream input, File to) throws IOException {

    String content = inputStreamFromString(input);
    if(!to.exists()) {
        if(!to.createNewFile()) {
            return false;
        }
    }
    FileWriter writer = new FileWriter(to);
    writer.write(content);
    writer.close();
    return true;

    }

    /**
     *
     * Copies the config.yml to another temp file.
     *
     * @why I use this for prevent that the file config.yml loses comments ( # )
     * @return the new temp file
     */
    public File copyConfiguration() {

        OS os = getCurrentOs();

        try (InputStream fileStream = inputStreamFromFile(new File(main.getDataFolder(), "config.yml"))) {


            String randomName = main.randomString( 6).replace("\\", "e").replace("/", "Y");
            File file = new File(main.getDataFolder(), randomName + ".yml");

            if(os == null) {
                main.logConsole("You are running an Unknown OS!");
                main.logConsole("Using the WinFileWriter as default.");
                Files.copy(fileStream, file.toPath());
                fileStream.close();
                last = file;
                return new File(main.getDataFolder(), randomName + ".yml");
            }

            if(os == OS.LINUX) {

                try {

                    if(linuxWrite(fileStream, file)) {
                        return new File(main.getDataFolder(), randomName + ".yml");
                    } else {
                        main.logConsole("Unable to create the temp file.");
                        return null;
                    }

                } catch (Exception exc) {
                    exc.printStackTrace();
                    main.logConsole("Unable to create the temp file.");
                    return null;
                }

            } else if(os == OS.WIN || os == OS.MAC) {

                Files.copy(fileStream, file.toPath());
                fileStream.close();
                last = file;
                return new File(main.getDataFolder(), randomName + ".yml");
            } else if(os == OS.UNKNOWN) {
                    main.logConsole("You are running an Unknown OS!");
                    main.logConsole("Using the WinFileWriter as default.");
                Files.copy(fileStream, file.toPath());
                fileStream.close();
                last = file;
                return new File(main.getDataFolder(), randomName + ".yml");
            } else {
                main.logConsole("You are running an Unknown OS!");
                main.logConsole("Using the WinFileWriter as default.");
                Files.copy(fileStream, file.toPath());
                fileStream.close();
                last = file;
                return new File(main.getDataFolder(), randomName + ".yml");
            }
        } catch(Exception exc) {
            exc.printStackTrace();
            main.logConsole("Unable to create the temp file.");
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
