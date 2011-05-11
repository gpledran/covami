package controllers;

import java.util.ArrayList;
import java.util.Collections;

import models.Ville;


@SuppressWarnings("rawtypes")
public class Noeud implements Comparable {
	private Ville ville;
	private double heuristique;
	private double distReelle;
	private Noeud noeudParent;

	/*
	 * Constructeur
	 */
	public Noeud(Ville ville, double d, double distReelle, Noeud nParent) {
		this.ville = ville;
		this.heuristique = d;
		this.distReelle = distReelle;
		this.noeudParent = nParent;
	}

	/*
	 * Rend la liste sortable
	 */
	@Override
	public int compareTo(Object other) {
		return Double.compare(this.getHeuristique(),
				((Noeud) other).getHeuristique());
	}

	/*
	 * Inserer suivant l'heuristique
	 */
	public static void insererNoeudHeuristique(Noeud n,
			ArrayList<Noeud> fileAStar) {
		// Ajoute le noeurd dans la liste de noeud AStar
		fileAStar.add(n);
		// Tri la liste suivant l'attribut heuristique
		Collections.sort(fileAStar);
	}

	/*
	 * Getters & Setters
	 */
	public Ville getVille() {
		return ville;
	}

	public void setVille(Ville ville) {
		this.ville = ville;
	}

	public double getHeuristique() {
		return heuristique;
	}

	public void setHeuristique(int heuristique) {
		this.heuristique = heuristique;
	}

	public double getDistReelle() {
		return distReelle;
	}

	public void setDistReelle(int distReelle) {
		this.distReelle = distReelle;
	}

	public void setNoeudParent(Noeud noeudParent) {
		this.noeudParent = noeudParent;
	}

	public Noeud getNoeudParent() {
		return noeudParent;
	}

}
