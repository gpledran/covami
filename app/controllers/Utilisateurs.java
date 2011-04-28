package controllers;

import models.Utilisateur;
import play.*;
import play.data.validation.Required;
import play.mvc.*;

public class Utilisateurs extends Controller {
	@Before
	static void addDefaults() {
		renderArgs.put("covamiTitle",
				Play.configuration.getProperty("covami.title"));
		renderArgs.put("covamiBaseline",
				Play.configuration.getProperty("covami.baseline"));
	}

	public static void moncompte() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user);
			renderArgs.put("security", Security.connected());
		}
		render();
	}

	public static void editermoncompte(Utilisateur usr) {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			render(user);
		}
		render();
	}

	public static void sauvegardermoncompte(@Required Utilisateur user) {
		validation.valid(user);
		if (validation.hasErrors()) {
			editermoncompte(user);
		} else {
			user.save(); // explicit save here
			flash.success("Sauvegarde réussie");
			moncompte();
		}
	}

	public static void inscription(Utilisateur user) {
		if (Security.isConnected()) {
			renderArgs.put("security", Security.connected());
			Application.index();
		}
		render(user);
	}

	public static void enregistrerinscription(Utilisateur user) {
		validation.valid(user);
		if (validation.hasErrors()) {
			/*
			 * for(play.data.validation.Error error : validation.errors()) {
			 * flash.error(error.message()); }
			 */
			flash.error("Erreur de validation");
			// flash.error(validation.toString());
			inscription(user);
		} else {
			if (Utilisateur.find("byEmail", user.email) != null) {
				flash.error("E-mail existant");
				inscription(user);
			} else {
				user.save(); // explicit save here
				flash.success("Inscription réussie - Merci de vous connecter");
				if (!Security.authentify(user.email, user.password)) {
					flash.error("Erreur d'authentification");
					return;
				}
				session.put("username", user.email);
				flash.success("Votre compte a bien été créé.");
				Application.index();
			}
		}
		renderArgs.put("user", user);
		renderArgs.put("security", Security.connected());
		Application.index();
	}

}
