package models;

import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Pays extends Model{

	@Required
	public String nom;

	public Pays(String nom) {
		this.nom = nom;
	}
	
}
