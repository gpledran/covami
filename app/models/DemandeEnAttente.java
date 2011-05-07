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
	public Long demandeur_id;

	@Required
	public Long receveur_id;

	public DemandeEnAttente(Long demandeur_id, Long receveur_id) {
		this.demandeur_id = demandeur_id;
		this.receveur_id = receveur_id;
	}
}
