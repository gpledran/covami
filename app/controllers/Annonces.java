package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.TypedQuery;

import org.codehaus.groovy.runtime.ConvertedClosure;

import models.Trajet;
import models.Utilisateur;
import models.Annonce;
import models.Ville;
import models.Voiture;
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
			flash.clear();
			renderArgs.put("annonces", annonces);

		}
		render();
	}

	public static void mesannonces(List<Annonce> mesAnnonces) {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			mesAnnonces = Annonce.find("byMonUtilisateur", moi).fetch();
			flash.clear();
			render(mesAnnonces);

		}
		render();
	}

	public static void editermonannonce(long id_annonce) {
		if (Security.isConnected()) {
			Annonce annonce = Annonce.findById(id_annonce);
			List<Ville> lesVilles = Ville.find("ORDER BY nom").fetch();
			renderArgs.put("annonce", annonce);
			renderArgs.put("etapes", annonce.monTrajet.mesEtapes);
			renderArgs.put("lesVilles", lesVilles);

		}
		render();
	}

	public static void sauvegardermonannonce(Annonce annonce, Long depart,
			Long arrivee, String date, String heure) {
		Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
				.first();
		annonce.monTrajet.villeDepart = Ville.findById(arrivee);
		annonce.monTrajet.villeArrivee = Ville.findById(depart);

		StringTokenizer st = new StringTokenizer(date, "/");
		String[] tabDate = new String[6];
		int i = 0;
		while (st.hasMoreTokens()) {
			tabDate[i] = st.nextToken();
			i++;
		}

		StringTokenizer stH = new StringTokenizer(heure, ":");
		while (stH.hasMoreTokens()) {
			tabDate[i] = stH.nextToken();
			i++;
		}
		annonce.monTrajet.dateDepart = new Date(
				Integer.parseInt(tabDate[2]) - 1900,
				Integer.parseInt(tabDate[1]) - 1, Integer.parseInt(tabDate[0]),
				Integer.parseInt(tabDate[3]), Integer.parseInt(tabDate[4]));
		// annonce.monTrajet.dateDepart = new SimpleDateFormat(DateFormat)
		// (tabDate[2] + "-"
		// + tabDate[1] + "-" + tabDate[0] + " " + tabDate[3] + ":" +
		// tabDate[4]);
		annonce.monTrajet.save();
		annonce.save();
		flash.success("Sauvegarde réussie");
		List<Annonce> mesAnnonces = Annonce.find("byMonUtilisateur", moi)
				.fetch();
		mesannonces(mesAnnonces);
	}

	public static void details(long id_annonce) {
		if (Security.isConnected()) {
			Annonce annonce = Annonce.find("byId", id_annonce).first();
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			renderArgs.put("moi", moi);
			renderArgs.put("annonce", annonce);
			renderArgs.put("etapes", annonce.monTrajet.mesEtapes);

		}
		render();
	}

	public static void recherche(String field) {
		if (Security.isConnected()) {
			List<String> s = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(field, ", ");
			while (st.hasMoreTokens()) {
				s.add(st.nextToken());
			}
			List<Annonce> annonces = new ArrayList<Annonce>();
			// todo

			render(annonces);
		}
		render();
	}

	public static void creation(Annonce annonce, Trajet trajet) {
		if (Security.isConnected()) {
			annonce.monUtilisateur = Utilisateur.find("byEmail",
					Security.connected()).first();
			List<Ville> lesVilles = Ville.find("ORDER BY nom").fetch();
			render(annonce, trajet, lesVilles);
		}
		render();
	}

	public static void enregistrerannonce(Annonce annonce,
			String villeDepart_insee, String villeArrivee_insee,
			String dateDepart, String heureDepart, String tarifTotal,
			List<Ville> etapes) {
		// validation.valid(annonce);
		// validation.valid(trajet);
		System.out.println("test creation 1");
		/*
		 * if (validation.hasErrors()) {
		 * 
		 * params.flash();
		 * 
		 * validation.keep(); creation(annonce, trajet); }else{
		 */
		// System.out.println("test creation 2");
		// trajet.save();
		// annonce.monTrajet = trajet;
		Ville villeDepart = Ville.find("byCodeInsee", villeDepart_insee)
				.first();
		Ville villeArrivee = Ville.find("byCodeInsee", villeArrivee_insee)
				.first();
		// etapes.add(villeDepart);

		annonce.tarifParPersonne = Integer.parseInt(tarifTotal);

		Trajet monTrajet = new Trajet(new Date(), villeDepart, villeArrivee,
				etapes);

		// annonce.save();
		flash.success("Annonce enregistrée");
		annonces();
	}
}