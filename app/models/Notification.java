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
	public int accepter;

	public Notification(Annonce monAnnonce, Utilisateur monUtilisateur,
			int accepter) {
		super();
		this.monAnnonce = monAnnonce;
		this.monUtilisateur = monUtilisateur;
		this.accepter = accepter;
	}

}
