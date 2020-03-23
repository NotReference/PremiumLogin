package it.notreference.bungee.premiumlogin.api;

//Codes:
//0 - UUID Error (Not showed anymore)
//1 - Success
//2 - No Premium
//3 - Not Online
//4 - No Premium Connection but is premium.
//5 - Arleady logged in. (Not showed anymore)
//6 - Other error..
//7 - User logged in legacy mode && non consentito nel config.

public enum LoginResponse {
 SUCCESS, UNABLE, ERROR, NOTONLINE, NOLEGACY, NOPREMIUM, NEEDSTOLOGINWITHLAUNCHER
}
