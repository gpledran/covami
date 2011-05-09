package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.TypedQuery;

import org.codehaus.groovy.runtime.ConvertedClosure;

import com.sun.jmx.snmp.Timestamp;

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

	public static void mesannonces() {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			List<Annonce> mesAnnonces = Annonce.find("byMonUtilisateur", moi)
					.fetch();
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

	public static Date retournerDate(String date, String heure)
			throws ParseException {
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

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return (formatter.parse(tabDate[2] + "-" + tabDate[1] + "-"
				+ tabDate[0] + " " + tabDate[3] + ":" + tabDate[4]));

	}

	public static void sauvegardermonannonce(Annonce annonce, Long depart,
			Long arrivee, @Required String date, @Required String heure)
			throws ParseException {
		// Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
		// .first();
		validation.valid(date);
		validation.valid(heure);
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			editermonannonce(annonce.id);
		} else {
			annonce.monTrajet.villeDepart = Ville.findById(arrivee);
			annonce.monTrajet.villeArrivee = Ville.findById(depart);
			annonce.monTrajet.dateDepart = retournerDate(date, heure);

			annonce.monTrajet.save();
			annonce.save();
			flash.success("Sauvegarde réussie");
			// List<Annonce> mesAnnonces = Annonce.find("byMonUtilisateur", moi)
			// .fetch();
			mesannonces();
		}
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
			List<Annonce> pAnnonces = new ArrayList<Annonce>();
			field = "%"+field+"%";
			List<Utilisateur> utilisateurs = Utilisateur.find("nom like ? or prenom like ?", field, field).fetch();

			for(Utilisateur u : utilisateurs){
				List<Annonce> l = Annonce.find("byMonUtilisateur_id",u.id).fetch();
				for (Annonce a : l) {
					pAnnonces.add(a);
				}
			}
			
			renderArgs.put("annonces", pAnnonces);
			renderTemplate("Annonces/annonces.html");
		}
		
		render();
	}

	public static void creation(Annonce annonce) {
		if (Security.isConnected()) {
			List<Ville> lesVilles = Ville.find("ORDER BY nom").fetch();
			render(annonce, lesVilles);
		}
		render();
	}

	public static void enregistrerannonce(Annonce annonce,
			@Required String villeDepart_insee, @Required String villeArrivee_insee,
			@Required String dateDepart, @Required String heureDepart, String tarifTotal,
			List<Ville> etapes) throws ParseException {
		 
		validation.valid(dateDepart);
		validation.valid(heureDepart);
		//validation.valid(villeDepart_insee);
		//validation.valid(villeArrivee_insee);
	
		if (validation.hasErrors()) {
		  params.flash();
		  validation.keep(); 
		  creation(annonce);
		}else{
			System.out.println("le programme est passé ici");
			Ville villeDepart = Ville.find("byCodeInsee", villeDepart_insee)
					.first();
			Ville villeArrivee = Ville.find("byCodeInsee", villeArrivee_insee)
					.first();
	
			Date madate = retournerDate(dateDepart, heureDepart);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			dateFormat.format(madate);
	
			Trajet monTrajet = new Trajet(madate, villeDepart, villeArrivee, etapes);
			annonce.monTrajet = monTrajet;
			annonce.tarifParPersonne = Integer.parseInt(tarifTotal);
			annonce.monUtilisateur = Utilisateur.find("byEmail",
					Security.connected()).first();
	
			monTrajet.save();
			annonce.save();
	
			flash.success("Annonce enregistrée");
			mesannonces();

		}
	}
}