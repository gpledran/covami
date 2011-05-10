package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Trajet extends Model {
	@Required
	public Date dateDepart;

	@OneToOne
	public Ville villeDepart;

	@OneToOne
	public Ville villeArrivee;

	@ManyToMany
	public List<Ville> mesEtapes;

	public Trajet(Date dateDepart, Ville villeDepart, Ville villeArrivee,
			List<Ville> mesEtapes) {
		super();
		this.dateDepart = dateDepart;
		this.villeDepart = villeDepart;
		this.villeArrivee = villeArrivee;
		this.mesEtapes = mesEtapes;
	}
}
