package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Voiture extends Model {
	@Required
	public String type;

	@Required
	public String nbPlaces;

	// @Required
	public String gabarit;

	public Voiture(String type, String nbPlaces, String gabarit) {
		this.type = type;
		this.nbPlaces = nbPlaces;
		this.gabarit = gabarit;
	}

	public String toString() {
		return this.type + " - " + this.nbPlaces + " places.";
	}
}
