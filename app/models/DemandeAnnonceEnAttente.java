package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class DemandeAnnonceEnAttente extends Model {
	
	public Long Annonce_id;

	public Long mesDemandePassagers_id;
	
	public int nbPlacesRestantes;

	public DemandeAnnonceEnAttente(Long annonce_id,
			Long mesDemandePassagers_id, int nbPlacesRestantes) {
		super();
		Annonce_id = annonce_id;
		this.mesDemandePassagers_id = mesDemandePassagers_id;
		this.nbPlacesRestantes = nbPlacesRestantes;
	}
	

}
