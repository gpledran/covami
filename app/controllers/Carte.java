package controllers;

import java.util.List;

import models.Annonce;
import models.Notification;
import models.Utilisateur;
import play.mvc.Before;
import play.mvc.Controller;

public class Carte extends Controller {
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

			List<Notification> mesNotifications = Notification.find(
					"byMonUtilisateur_id", user.id).fetch();

			nbDemandes += mesNotifications.size();

			renderArgs.put("nbDemandes", nbDemandes);
		}
	}

	public static void affichercarte() {
		render();
	}
}
