package controllers;

import models.Utilisateur;
import models.Voiture;
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

	public static void editermoncompte(Utilisateur usr, Voiture v) {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			render(user, v);
		}
		render();
	}

	public static void sauvegardermoncompte(@Required @Valid Utilisateur user,
			Voiture v) {
		validation.valid(user);
		if (validation.hasErrors()) {
			// add http parameters to the flash scope
			params.flash();
			// keep the errors for the next request
			validation.keep();
			editermoncompte(user, v);
		} else {
			v.save();
			user.maVoiture = v;
			user.save(); // explicit save here
			flash.success("Sauvegarde réussie");
			moncompte();
		}
	}

	public static void inscription(Utilisateur user, Voiture v) {
		if (Security.isConnected()) {
			Application.index();
		}
		render(user, v);
	}

	public static void enregistrerinscription(@Valid Utilisateur user, Voiture v) {
		validation.valid(user);
		// validation.valid(v);
		if (validation.hasErrors()) {
			// add http parameters to the flash scope
			params.flash();
			// keep the errors for the next request
			validation.keep();
			inscription(user, v);
		} else {
			if (Utilisateur.find("byEmail", user.email).first() != null) {
				flash.error("E-mail existant");
				inscription(user, v);
			} else {
				// System.out.println("------------");
				v.save();
				user.maVoiture = v;
				user.save(); // explicit save here
				flash.success("Inscription réussie");
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
