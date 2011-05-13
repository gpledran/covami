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
	public List<MesPassagers> mesPassagers;

	@OneToMany
	public List<DemandeAnnonceEnAttente> mesDemandePassagers;

	public int placesRestantes;

	public Annonce(int tarifParPersonne, Utilisateur monUtilisateur,
			Trajet monTrajet, List<MesPassagers> mesPassagers,
			List<DemandeAnnonceEnAttente> mesDemandePassagers,
			int placesRestantes) {
		super();
		this.tarifParPersonne = tarifParPersonne;
		this.monUtilisateur = monUtilisateur;
		this.monTrajet = monTrajet;
		this.mesPassagers = mesPassagers;
		this.mesDemandePassagers = mesDemandePassagers;
		this.placesRestantes = placesRestantes;
	}

}
