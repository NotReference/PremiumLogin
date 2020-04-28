package it.notreference.spigot.premiumlogin.utils;

public class Messages {

    public static String parse(String f) {

			/*

	á, é, í, ó, ú

     ñ

	 */

        String nuovoMessaggio = f.replace("&", "§");


        if(nuovoMessaggio.contains(".N.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".N.", "ñ");
        }
        if(nuovoMessaggio.contains(".e1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".e1.", "è");
        }

        if(nuovoMessaggio.contains(".e2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".e2.", "é");
        }

        if(nuovoMessaggio.contains(".a1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".a1.", "à");
        }

        if(nuovoMessaggio.contains(".a2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".a2.", "á");
        }

        if(nuovoMessaggio.contains(".i1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "ì");
        }

        if(nuovoMessaggio.contains(".i2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "í");
        }

        if(nuovoMessaggio.contains(".o1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".o1.", "ò");
        }

        if(nuovoMessaggio.contains(".o2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".o2.", "ó");
        }

        if(nuovoMessaggio.contains(".u1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".u1.", "ù");
        }

        if(nuovoMessaggio.contains(".u2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".u2.", "ú");
        }

        return nuovoMessaggio;
    }

}
