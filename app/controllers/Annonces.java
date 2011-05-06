package controllers;

import java.util.List;

import javax.persistence.TypedQuery;

import models.Trajet;
import models.Utilisateur;
import models.Annonce;
import models.Ville;
import play.*;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Model;
import play.mvc.*;
import play.mvc.Scope.RenderArgs;

public class Annonces extends Controller {

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

	@Before
	static void mesDemandes() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			int nbDemandes = user.mesDemandes.size();
			renderArgs.put("nbDemandes", nbDemandes);
		}
	}

	public static void annonces() {
		if (Security.isConnected()) {
			List<Annonce> annonces = Annonce.findAll();

			renderArgs.put("annonces", annonces);

		}
		render();
	}

	public static void details(long id_annonce) {
		if (Security.isConnected()) {
			Annonce annonce = Annonce.find("byId", id_annonce).first();

			renderArgs.put("annonce", annonce);
			renderArgs.put("etapes", annonce.monTrajet.mesEtapes);

		}
		render();
	}

	public static void recherche(String field) {
		if (Security.isConnected()) {

		}
		render();
	}

	public static void creation(@Required @Valid Annonce annonce) {

	}
}