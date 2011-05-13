package controllers;

import play.*;
import play.data.validation.Required;
import play.db.DB;
import play.mvc.*;

import java.util.*;

import org.hibernate.mapping.Map;

import models.*;

public class Commentaires extends Controller {

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user);
			renderArgs.put("security", Security.connected());
		}
	}

	@Before
	static void nbDemandes() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			int nbDemandes = user.mesDemandes.size();

			List<Annonce> mesAnnonces = Annonce.find("byMonUtilisateur_id",
					user.id).fetch();
			for (Annonce a : mesAnnonces) {
				nbDemandes += a.mesDemandePassagers.size();
			}
			renderArgs.put("nbDemandes", nbDemandes);
		}
	}

	public static void commentaires(long id_user) {
		if (Security.isConnected()) {
			flash.clear();
			Utilisateur utilisateur = Utilisateur.findById(id_user);
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();

			List<Commentaire> commentaires = Commentaire.find(
					"byMonUtilisateur_id", utilisateur.id).fetch();
			Collections.reverse(commentaires);
			String valide = "false";
			// on verifie que 'moi' a deja effectué un trajet avec l'utilisateur
			List<Annonce> user_annonces = Annonce.find("byMonUtilisateur_id",
					utilisateur.id).fetch();
			for (Annonce annonce : user_annonces) {
				for (MesPassagers p : annonce.mesPassagers) {
					if (p.mesPassagers.id == moi.id
							&& annonce.monTrajet.dateDepart
									.compareTo(new Date()) < 0) {
						valide = "true";
					}
				}
			}
			System.out.println("valide = " + valide);

			render(commentaires, utilisateur, valide);
		}
		render();
	}

	public static void ajoutercommentaire(@Required String message,
			@Required String note, long id_user) {
		if (Security.isConnected()) {
			validation.valid(message);
			validation.valid(note);
			if (validation.hasErrors()) {
				params.flash();
				validation.keep();
				commentaires(id_user);
			} else {
				Utilisateur auteur = Utilisateur.find("byEmail",
						Security.connected()).first();
				Utilisateur utilisateur = Utilisateur.find("byId", id_user)
						.first();

				Commentaire com = new Commentaire(message,
						Integer.parseInt(note), utilisateur, auteur);

				com.save();

				commentaires(id_user);
			}
		}
		render();
	}

}