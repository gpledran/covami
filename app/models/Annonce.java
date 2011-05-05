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
}
