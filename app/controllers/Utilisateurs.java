package controllers;

import models.Utilisateur;
import play.*;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

public class Utilisateurs extends Controller {
	@Before
	static void addDefaults() {
		renderArgs.put("covamiTitle",
				Play.configuration.getProperty("covami.title"));
		renderArgs.put("covamiBaseline",
				Play.configuration.getProperty("covami.baseline"));
	}

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user);
			renderArgs.put("security", Security.connected());
			flash.success("Connexion réussie");
		}
	}

	public static void moncompte() {
		flash.clear();
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

	public static void sauvegardermoncompte(@Required @Valid Utilisateur user) {
		validation.valid(user);
		if (validation.hasErrors()) {
			// add http parameters to the flash scope
			params.flash();
			// keep the errors for the next request
			validation.keep();
			editermoncompte(user);
		} else {
			user.save(); // explicit save here
			flash.success("Sauvegarde réussie");
			moncompte();
		}
	}

	public static void inscription(Utilisateur user) {
		if (Security.isConnected()) {
			Application.index();
		}
		render(user);
	}

	public static void enregistrerinscription(@Valid Utilisateur user) {
		validation.valid(user);
		if (validation.hasErrors()) {
			// add http parameters to the flash scope
			params.flash();
			// keep the errors for the next request
			validation.keep();
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
		Application.index();
	}

}
