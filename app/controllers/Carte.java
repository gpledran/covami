package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import models.Annonce;
import models.Notification;
import models.Trajet;
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
		Date d = new Date();

		List<Trajet> lesTrajets = Trajet.find("dateDepart > ?", d).fetch();

		String lesVilles = "";
		boolean premier = true;
		for (Trajet t : lesTrajets) {
			if (premier) {
				lesVilles += t.villeDepart.nom + ", " + t.villeArrivee.nom;
				premier = false;
			} else {
				lesVilles += ", " + t.villeDepart.nom + ", "
						+ t.villeArrivee.nom;
			}
		}
		render(lesVilles);
	}
}
