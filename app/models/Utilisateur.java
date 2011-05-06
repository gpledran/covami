package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Utilisateur extends Model {
	@Required
	@Email
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

	@OneToMany
	public List<Utilisateur> mesAmis;

	@OneToMany
	@JoinTable(name = "DemandeEnAttente")
	public List<Utilisateur> mesDemandes;

	public ArrayList<String> mesCriteres;

	public Utilisateur(String email, String password, String nom,
			String prenom, String telephoneMobile, String telephonePerso,
			Date dateNaissance, String rue, String cp, String ville,
			String pays, Voiture maVoiture, List<Utilisateur> mesAmis,
			List<Utilisateur> mesDemandes, ArrayList<String> mesCriteres) {
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
		this.mesAmis = mesAmis;
		this.mesDemandes = mesDemandes;
		this.mesCriteres = mesCriteres;
	}

	public static Utilisateur connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public String toString() {
		return this.prenom + " " + this.nom;
	}

}
