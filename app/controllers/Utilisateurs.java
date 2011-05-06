package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import models.DemandeEnAttente;
import models.Utilisateur;
import models.Voiture;
import play.*;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

public class Utilisateurs extends Controller {

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
	static void nbDemandes() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			int nbDemandes = user.mesDemandes.size();
			renderArgs.put("nbDemandes", nbDemandes);
		}
	}

	public static void moncompte() {
		flash.clear();
		render();
	}

	public static void editermoncompte(Utilisateur usr, Voiture v, String c1,
			String c2, String c3) {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			v = user.maVoiture;
			if (user.mesCriteres != null) {
				for (String c : user.mesCriteres) {
					if (c.equals("animaux")) {
						c1 = c;
					} else if (c.equals("musique")) {
						c2 = c;
					} else if (c.equals("fumeur")) {
						c3 = c;
					}
				}
			}
			render(user, v, c1, c2, c3);
		}
		render();
	}

	public static void sauvegardermoncompte(@Required @Valid Utilisateur user,
			Voiture v, String c1, String c2, String c3) {
		validation.valid(user);
		if (validation.hasErrors()) {
			// add http parameters to the flash scope
			params.flash();
			// keep the errors for the next request
			validation.keep();
			editermoncompte(user, v, c1, c2, c3);
		} else {
			if (v.type.equals("-1") || v.nbPlaces.equals("-1")) {
				user.maVoiture = null;
			} else {
				v.save();
				user.maVoiture = v;
			}
			ArrayList<String> mesCriteres = new ArrayList<String>();
			if (c1 != null && c1.equals("animaux")) {
				mesCriteres.add("animaux");
			}
			if (c2 != null && c2.equals("musique")) {
				mesCriteres.add("musique");
			}
			if (c3 != null && c3.equals("fumeur")) {
				mesCriteres.add("fumeur");
			}
			if (!mesCriteres.isEmpty()) {
				user.mesCriteres = mesCriteres;
			}
			user.save(); // explicit save here
			flash.success("Sauvegarde réussie");
			moncompte();
		}
	}

	public static void inscription(Utilisateur user, Voiture v, String c1,
			String c2, String c3) {
		if (Security.isConnected()) {
			Application.index();
		}
		render(user, v, c1, c2, c3);
	}

	public static void enregistrerinscription(@Valid Utilisateur user,
			Voiture v, String c1, String c2, String c3) {
		validation.valid(user);
		// validation.valid(v);
		if (validation.hasErrors()) {
			// add http parameters to the flash scope
			params.flash();
			// keep the errors for the next request
			validation.keep();
			inscription(user, v, c1, c2, c3);
		} else {
			if (Utilisateur.find("byEmail", user.email).first() != null) {
				flash.error("E-mail existant");
				inscription(user, v, c1, c2, c3);
			} else {
				if (v.type.equals("-1") || v.nbPlaces.equals("-1")) {
					user.maVoiture = null;
				} else {
					v.save();
					user.maVoiture = v;
				}
				ArrayList<String> mesCriteres = new ArrayList<String>();
				if (c1 != null && c1.equals("animaux")) {
					mesCriteres.add("animaux");
				}
				if (c2 != null && c2.equals("musique")) {
					mesCriteres.add("musique");
				}
				if (c3 != null && c3.equals("fumeur")) {
					mesCriteres.add("fumeur");
				}
				if (!mesCriteres.isEmpty()) {
					user.mesCriteres = mesCriteres;
				}
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

	public static void supprimermoncompte() throws Throwable {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			user.delete();
			Secure.logout();
		}
		Application.index();
	}

	public static void mesamis() {
		flash.clear();
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			List<Utilisateur> mesAmis = user.mesAmis;
			render(mesAmis);
		}
		render();
	}

	public static void recherche(String field) {
		System.out.println("=========test========");
		if (Security.isConnected()) {
			List<String> s = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(field, ", ");
			while (st.hasMoreTokens()) {
				s.add(st.nextToken());
			}
			List<Utilisateur> mesAmis = new ArrayList<Utilisateur>();
			if (s.size() == 3) {
				mesAmis = Utilisateur.find(
						"email like ? or nom like ? or prenom like ?",
						"%" + s.get(2) + "%", "%" + s.get(0) + "%",
						"%" + s.get(1) + "%").fetch();
			} else if (s.size() == 2) {
				mesAmis = Utilisateur.find("nom like ? or prenom like ?",
						"%" + s.get(0) + "%", "%" + s.get(1) + "%").fetch();
			} else if (s.size() == 1) {
				mesAmis = Utilisateur.find("nom like ?", "%" + s.get(0) + "%")
						.fetch();
			} else if (s.size() == 0) {
				mesAmis = Utilisateur.findAll();
			}
			System.out.println("======================");
			System.out.println(mesAmis.get(0).email);
			render(mesAmis);
		}
		render();
	}

	public static void envoyerdemande(Long id) {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			Utilisateur ami = Utilisateur.findById(id);
			ami.mesDemandes.add(user);
			ami.save();
			// (new DemandeEnAttente(user.id, Long.parseLong(id))).save();
			flash.success("Demande envoyée avec succès.");
			Application.index();
		}
	}

	public static void mesdemandes() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			List<Utilisateur> mesDemandes = user.mesDemandes;
			render(mesDemandes);
		}
		render();
	}

	public static void accepterdemande(Long id) {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			Utilisateur ami = Utilisateur.findById(id);
			moi.mesAmis.add(ami);
			ami.mesAmis.add(moi);
			moi.mesDemandes.remove(ami);
			moi.save();
			ami.save();
			flash.success("Ami ajouté avec succès.");
			mesamis();
		}
		render();
	}

	public static void refuserdemande(Long id) {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			Utilisateur ami = Utilisateur.findById(id);
			moi.mesDemandes.remove(ami);
			moi.save();
			flash.success("Ami refusé avec succès.");
			mesamis();
		}
		render();
	}

	public static void supprimerami(Long id) {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			Utilisateur ami = Utilisateur.findById(id);
			moi.mesAmis.remove(ami);
			ami.mesAmis.remove(moi);
			moi.save();
			ami.save();
			mesamis();
		}
		render();
	}
}
