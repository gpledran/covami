package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Annonce extends Model {
	@Required
	public int tarifParPersonne;

	@OneToOne
	public Utilisateur monUtilisateur;

	@OneToOne
	public Trajet monTrajet;

	public Annonce(int tarifParPersonne, Utilisateur monUtilisateur,
			Trajet monTrajet) {
		super();
		this.tarifParPersonne = tarifParPersonne;
		this.monUtilisateur = monUtilisateur;
		this.monTrajet = monTrajet;
	}
	
	public int calculerTarifTotal(){
		// tester si l'utilisateur à une voiture ?
		int tarifTotal = 0, kmsTotal = 0;
		for(int i = 0 ; i < this.monTrajet.mesEtapes.size()-1 ; i++){
			Troncon troncon = Troncon.find("byVilleActuelle_idAndVilleSuivante_id", this.monTrajet.mesEtapes.get(i).id, this.monTrajet.mesEtapes.get(i+1)).first();
			kmsTotal += troncon.nbKms;
		}
		// coefficient d'essence
		if(this.monUtilisateur.maVoiture.type == "petite")
			tarifTotal = (int)Math.ceil(kmsTotal/10);
		else if(this.monUtilisateur.maVoiture.type == "moyenne")
			tarifTotal = (int)Math.ceil((kmsTotal/10)*1.25);
		else if(this.monUtilisateur.maVoiture.type == "grande")
			tarifTotal = (int)Math.ceil((kmsTotal/10)*1.50);
		
		return tarifTotal;
	}
}
