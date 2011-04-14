package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Voiture extends Model{
	@Required
	public String type;
	
	@Required 
	public String nbPlaces;

	public Voiture(String type, String nbPlaces) {
		this.type = type;
		this.nbPlaces = nbPlaces;
	}
	
    public String toString() {
        return this.type + " - " + this.nbPlaces + " places.";
    }
}
