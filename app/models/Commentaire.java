package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Commentaire extends Model {
	@Required
	public String message;

	@Required
	public int note;

	@OneToOne
	public Utilisateur monUtilisateur;

	public Commentaire(String message, int note, Utilisateur monUtilisateur) {
		super();
		this.message = message;
		this.note = note;
		this.monUtilisateur = monUtilisateur;
	}

}
