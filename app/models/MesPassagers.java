package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class MesPassagers extends Model {
	@OneToOne
	@Required
	public Annonce Annonce;

	@OneToOne
	@Required
	public Utilisateur mesPassagers;

	@Required
	public int nbPassagers;

	@OneToOne
	@Required
	public Ville villeDebut;

	@OneToOne
	@Required
	public Ville villeFin;

	@Required
	public int prixParPassager;

	public MesPassagers(models.Annonce annonce, Utilisateur mesPassagers,
			int nbPassagers, Ville villeDebut, Ville villeFin,
			int prixParPassager) {
		super();
		Annonce = annonce;
		this.mesPassagers = mesPassagers;
		this.nbPassagers = nbPassagers;
		this.villeDebut = villeDebut;
		this.villeFin = villeFin;
		this.prixParPassager = prixParPassager;
	}

}
