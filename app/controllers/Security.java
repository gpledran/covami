package controllers;
 
import models.*;
 
public class Security extends Secure.Security {
 
	static boolean authentify(String email, String password) {
	    return Utilisateur.connect(email, password) != null;
	}
	
	static void onDisconnected() {
	    Application.index();
	}
	
	static void onAuthenticated() {
	    Application.index();
	}
    
}