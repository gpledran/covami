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
	
	@OneToMany
	public List<Utilisateur> mesPassagers;
	
	@ManyToMany
	@JoinTable(name = "DemandeAnnonceEnAttente")
	public List<Utilisateur> mesDemandePassagers;
	
	
	public int placesRestantes;

	public Annonce(int tarifParPersonne, Utilisateur monUtilisateur,
			Trajet monTrajet, int placesRestantes) {
		super();
		this.tarifParPersonne = tarifParPersonne;
		this.monUtilisateur = monUtilisateur;
		this.monTrajet = monTrajet;
		this.placesRestantes = placesRestantes;
	}
}
