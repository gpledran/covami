import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

	@SuppressWarnings("deprecation")
	public void doJob() {
		// Verifie que la table Utilisateur soit vide
		if (Ville.count() == 0) {
			initialiserVilles();
		}
		if (Utilisateur.count() == 0) {
			Fixtures.load("initial-data.yml");
		}

	}

	public void initialiserVilles() {
		Pays france = Pays.find("byNom", "France").first();
		try {
			BufferedReader fichier = new BufferedReader(new FileReader(
					"public/csv/villes.csv"));
			String chaine;
			while ((chaine = fichier.readLine()) != null) {
				// Sépare à l'aide du ; la ligne dans un tableau de chaines
				String[] champs = chaine.split(";");
				// Ajout de la ville en cours
				(new Ville(champs[0], champs[1], Float.parseFloat(champs[3]),
						Float.parseFloat(champs[4]), france)).save();
			}
			fichier.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}