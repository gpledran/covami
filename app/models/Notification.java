package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Notification extends Model {

	@Required
	@OneToOne
	public Annonce monAnnonce;

	@Required
	@OneToOne
	public Utilisateur monUtilisateur;

	@Required
	@OneToOne
	public Utilisateur monNotifieur;

	@Required
	public int accepter;

	public Notification(Annonce monAnnonce, Utilisateur monUtilisateur,
			Utilisateur monNotifieur, int accepter) {
		super();
		this.monAnnonce = monAnnonce;
		this.monUtilisateur = monUtilisateur;
		this.monNotifieur = monNotifieur;
		this.accepter = accepter;
	}
}
