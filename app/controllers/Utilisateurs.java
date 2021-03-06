package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import models.Annonce;
import models.Commentaire;
import models.DemandeAnnonceEnAttente;
import models.DemandeEnAttente;
import models.MesPassagers;
import models.Notification;
import models.Utilisateur;
import models.Voiture;
import play.*;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;
import play.mvc.Scope.RenderArgs;

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
			flash.clear();
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
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			List<Utilisateur> liste = Utilisateur.findAll();
			for (Utilisateur u : liste) {
				if (u.mesAmis.contains(moi)) {
					u.mesAmis.remove(moi);
					moi.mesAmis.remove(u);
					u.save();
					moi.save();
				}
				if (u.mesDemandes.contains(moi)) {
					u.mesDemandes.remove(moi);
					moi.mesDemandes.remove(u);
					u.save();
					moi.save();
				}
			}
			List<Annonce> annonces = Annonce.findAll();
			for (Annonce a : annonces) {
				if (a.monUtilisateur.equals(moi)) {
					a.delete();
				}
			}
			moi.delete();
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
			flash.clear();
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			mesAmis.remove(moi);
			for (Utilisateur ami : moi.mesAmis) {
				mesAmis.remove(ami);
			}
			for (Utilisateur amis : moi.mesDemandes) {
				mesAmis.remove(amis);
			}
			List<Utilisateur> listeDemandes = Utilisateur.findAll();
			for (Utilisateur demande : listeDemandes) {
				if (demande.mesDemandes.contains(moi)) {
					mesAmis.remove(demande);
				}
			}
			render(mesAmis);
		}
		render();
	}

	public static void mescovoiturages() {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			List<Annonce> annonces = Annonce.findAll();
			List<Annonce> mescovoiturages = new ArrayList<Annonce>();
			for (Annonce a : annonces) {
				for (MesPassagers p : a.mesPassagers) {
					if (p.mesPassagers.id == moi.id) {
						mescovoiturages.add(a);
					}
				}
			}
			flash.clear();
			renderArgs.put("moi_id",moi.id);
			render(mescovoiturages);
		}
		render();
	}

	public static void envoyerdemande(Long id) {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			Utilisateur ami = Utilisateur.findById(id);
			ami.mesDemandes.add(moi);
			ami.save();
			flash.success("Demande envoyée avec succès.");
			Application.index();
		}
	}

	// mes demande ami + participation annonce
	public static void mesdemandes() {
		if (Security.isConnected()) {
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected())
					.first();
			List<Utilisateur> mesDemandes = moi.mesDemandes;
			List<Annonce> mesAnnonces = Annonce.find("byMonUtilisateur_id",
					moi.id).fetch();

			List<Notification> mesNotifications = Notification.find(
					"byMonUtilisateur_id", moi.id).fetch();

			flash.clear();
			render(mesDemandes, mesAnnonces, moi, mesNotifications);
		}
		render();
	}

	public static void supprimernotification(Long id) {
		if(Security.isConnected()){
			Notification notif = Notification.findById(id);
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected()).first();
			moi.mesNotifications.remove(notif);
			moi.save();
			Notification.delete("id like ?", id);
			mesdemandes();
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

	public static void accepterDemandeAnnonce(Long id_demande) {
		if (Security.isConnected()) {
			DemandeAnnonceEnAttente demande = DemandeAnnonceEnAttente
					.findById(id_demande);

			Annonce monAnnonce = demande.Annonce;
			Utilisateur passager = demande.mesDemandePassagers;

			MesPassagers monPassager = new MesPassagers(monAnnonce, passager,
					demande.nbPassagers, demande.villeDebut, demande.villeFin,
					demande.prixParPassager);

			monPassager.save();

			monAnnonce.mesPassagers.add(monPassager);
			monAnnonce.placesRestantes -= demande.nbPassagers;
			monAnnonce.mesDemandePassagers.remove(demande);

			monAnnonce.save();

			Utilisateur notifieur = Utilisateur.find("byEmail",
					Security.connected()).first();

			Notification notif = new Notification(monAnnonce, passager,
					notifieur, 1);
			notif.save();

			passager.mesNotifications.add(notif);
			passager.save();

			demande.delete();
			flash.success("Passager accepté");
			mesdemandes();
		}
		render();
	}

	public static void refuserDemandeAnnonce(Long id_demande) {
		if (Security.isConnected()) {
			flash.clear();
			DemandeAnnonceEnAttente demande = DemandeAnnonceEnAttente
					.findById(id_demande);

			Utilisateur passager = demande.mesDemandePassagers;

			Annonce monAnnonce = demande.Annonce;

			monAnnonce.mesDemandePassagers.remove(demande);

			monAnnonce.save();

			Utilisateur notifieur = Utilisateur.find("byEmail",
					Security.connected()).first();

			Notification notif = new Notification(monAnnonce, passager,
					notifieur, 0);
			notif.save();

			passager.mesNotifications.add(notif);
			passager.save();

			demande.delete();
			flash.success("Passager refusé");
			mesdemandes();
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
	
	public static void profil(long id){
		if(Security.isConnected()){
			flash.clear();
			Utilisateur u = Utilisateur.findById(id);
			Utilisateur moi = Utilisateur.find("byEmail", Security.connected()).first();
			//on verifie si l'utilisateur est dans ma liste d'ami
			boolean estPresentDansListeAmi = false;
			for(Utilisateur ami : u.mesAmis){
				if(ami.id == moi.id){
					estPresentDansListeAmi = true;
				}
			}
			render(u, estPresentDansListeAmi);
		}
	}
}
