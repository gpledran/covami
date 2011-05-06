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
	public static void annonces(){
		if(Security.isConnected()){
			List <Annonce> annonces = Annonce.findAll();
		
			renderArgs.put("annonces",annonces);
			
		}
		render();
	}
	public static void details(long id_annonce){
		if(Security.isConnected()){
			Annonce annonce = Annonce.find("byId",id_annonce).first();
			
			renderArgs.put("annonce", annonce);
			renderArgs.put("etapes", annonce.monTrajet.mesEtapes);
			
		}
		render();
	}
	public static void recherche(String field){
		if(Security.isConnected()){
			
		}
		render();
	}
	public static void creation(Annonce annonce, Trajet trajet){
		if(Security.isConnected()){
			annonce.monUtilisateur = Utilisateur.find("byEmail", Security.connected()).first();
			List <Ville> lesVilles = Ville.findAll();
			render(annonce, trajet, lesVilles);
		}
		render();
	}
	public static void enregistrerannonce(Annonce annonce, String villeDepart_id){
		//validation.valid(annonce);
		//validation.valid(trajet);
		System.out.println("test creation 1");
		/*if (validation.hasErrors()) {
			
			params.flash();
			
			validation.keep();
			creation(annonce, trajet);
		}else{*/
			//System.out.println("test creation 2");
			//trajet.save();
			//annonce.monTrajet = trajet;
			System.out.println("depart : "+villeDepart_id);

			//annonce.save(); 
			flash.success("Annonce enregistrée");

			//Application.index();
		//}
	}
}