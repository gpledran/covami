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
	public int tarif;

	@Required
	public int nbKms;

	public Troncon(String nomAutoroute, Ville villeSuivante, int tarif,
			int nbKms) {
		this.nomAutoroute = nomAutoroute;
		this.villeSuivante = villeSuivante;
		this.tarif = tarif;
		this.nbKms = nbKms;
	}

}
