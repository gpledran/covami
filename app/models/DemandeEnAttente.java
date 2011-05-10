package models;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class DemandeEnAttente extends Model {

	@Required
	public Long Utilisateur_id;

	@Required
	public Long mesDemandes_id;

	public DemandeEnAttente(Long demandeur_id, Long receveur_id) {
		this.Utilisateur_id = demandeur_id;
		this.mesDemandes_id = receveur_id;
	}
}
