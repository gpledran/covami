package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class DemandeAnnonceEnAttente extends Model {

	@Required
	@OneToOne
	public Annonce Annonce;

	@Required
	@OneToOne
	public Utilisateur mesDemandePassagers;

	@Required
	public int nbPassagers;

	@Required
	@OneToOne
	public Ville villeDebut;

	@OneToOne
	@Required
	public Ville villeFin;

	@Required
	public int prixParPassager;

	public DemandeAnnonceEnAttente(models.Annonce annonce,
			Utilisateur mesDemandePassagers, int nbPassagers, Ville villeDebut,
			Ville villeFin, int prixParPassager) {
		super();
		Annonce = annonce;
		this.mesDemandePassagers = mesDemandePassagers;
		this.nbPassagers = nbPassagers;
		this.villeDebut = villeDebut;
		this.villeFin = villeFin;
		this.prixParPassager = prixParPassager;
	}

}
