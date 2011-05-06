package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Ville extends Model {
	@Required
	public String nom;

	@Required
	public String codePostal;

	@Required
	public String codeInsee;

	@Required
	public float latitude;

	@Required
	public float longitude;

	@OneToOne
	public Pays monPays;

	public Ville(String nom, String codePostal, String codeInsee,
			float latitude, float longitude, Pays monPays) {
		this.nom = nom;
		this.codePostal = codePostal;
		this.codeInsee = codeInsee;
		this.latitude = latitude;
		this.longitude = longitude;
		this.monPays = monPays;
	}

}
