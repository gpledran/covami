package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Utilisateur extends Model {
	@Required
	public String email;

	@Required
	public String password;

	@Required
	public String nom;

	@Required
	public String prenom;

	public String telephoneMobile;

	public String telephonePerso;

	@Required
	public Date dateNaissance;

	@Required
	public String rue;

	@Required
	public String cp;

	@Required
	public String ville;

	@Required
	public String pays;

	@OneToOne
	public Voiture maVoiture;

	@OneToOne
	public Ville maVille;

	@OneToMany
	public List<Utilisateur> mesAmis;

	public ArrayList<String> mesCritères;

	public Utilisateur(String email, String password, String nom,
			String prenom, String telephoneMobile, String telephonePerso,
			Date dateNaissance, String rue, String cp, String ville,
			String pays, Voiture maVoiture, Ville maVille,
			List<Utilisateur> mesAmis) {
		super();
		this.email = email;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.telephoneMobile = telephoneMobile;
		this.telephonePerso = telephonePerso;
		this.dateNaissance = dateNaissance;
		this.rue = rue;
		this.cp = cp;
		this.ville = ville;
		this.pays = pays;
		this.maVoiture = maVoiture;
		this.maVille = maVille;
		this.mesAmis = mesAmis;
	}

	public static Utilisateur connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public String toString() {
		return this.prenom + " " + this.nom;
	}

}
