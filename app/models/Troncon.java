package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Troncon extends Model {

	@Required
	public String nomAutoroute;

	@Required
	@OneToOne
	public Ville villeSuivante;

	@Required
	@OneToOne
	public Ville villeActuelle;

	@Required
	public double nbKms;

	public Troncon(String nomAutoroute, Ville villeSuivante, Ville villeActuelle,
			double nbKms) {
		this.nomAutoroute = nomAutoroute;
		this.villeActuelle = villeActuelle;
		this.villeSuivante = villeSuivante;
		
		this.nbKms = nbKms;
	}

}
