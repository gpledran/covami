package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;

import java.util.*;

import org.hibernate.mapping.Map;

import models.*;

public class Application extends Controller {
	
	@Before
	static void addDefaults() {
	    renderArgs.put("covamiTitle", Play.configuration.getProperty("covami.title"));
	    renderArgs.put("covamiBaseline", Play.configuration.getProperty("covami.baseline"));
	}
	
	@Before
    static void setConnectedUser(){
         if(Security.isConnected()){
             Utilisateur user = Utilisateur.find("byEmail",Security.connected()).first();
             renderArgs.put("user", user);
             renderArgs.put("security",Security.connected());
             flash.success("Connexion réussie");
         }
     }

    public static void index() {
        render();
    }
    
    public static void moncompte() {
    	if(Security.isConnected()) {
    		Utilisateur user = Utilisateur.find("byEmail", Security.connected()).first();
    		renderArgs.put("user", user);
    		renderArgs.put("security",Security.connected());
    	}
    	render();
    }
    
    public static void editermoncompte() {
    	if(Security.isConnected()) {
    		Utilisateur user = Utilisateur.find("byEmail", Security.connected()).first();
    		//renderArgs.put("user", user);
    		renderArgs.put("security",Security.connected());
    		render(user);
    	}
    	render();
    }
    
    public static void sauvegardermoncompte(Utilisateur user) {
    	
    	flash.success("Sauvegarde réussie");
    	moncompte();
    }
    
    public static void inscription() {
    	if(Security.isConnected()) {
    		renderArgs.put("security",Security.connected());
    		index();
    	}
    	render();
    }
    
    public static void enregistrerinscription(Utilisateur user) {
    	user.save();
    	flash.success("Inscription réussie");
    	renderArgs.put("user", user);
    	renderArgs.put("security",Security.connected());
    	Utilisateur.connect(user.email, user.password);
    	index();
    }

}