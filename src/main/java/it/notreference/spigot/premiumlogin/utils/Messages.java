package it.notreference.spigot.premiumlogin.utils;

public class Messages {

    public static String parse(String f) {

			/*

	�, �, �, �, �

     �

	 */

        String nuovoMessaggio = f.replace("&", "�");


        if(nuovoMessaggio.contains(".N.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".N.", "�");
        }
        if(nuovoMessaggio.contains(".e1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".e1.", "�");
        }

        if(nuovoMessaggio.contains(".e2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".e2.", "�");
        }

        if(nuovoMessaggio.contains(".a1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".a1.", "�");
        }

        if(nuovoMessaggio.contains(".a2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".a2.", "�");
        }

        if(nuovoMessaggio.contains(".i1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".i1.", "�");
        }

        if(nuovoMessaggio.contains(".i2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".i2.", "�");
        }

        if(nuovoMessaggio.contains(".o1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".o1.", "�");
        }

        if(nuovoMessaggio.contains(".o2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".o2.", "�");
        }

        if(nuovoMessaggio.contains(".u1.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".u1.", "�");
        }

        if(nuovoMessaggio.contains(".u2.")) {
            nuovoMessaggio = nuovoMessaggio.replaceAll(".u2.", "�");
        }

        return nuovoMessaggio;
    }

}
