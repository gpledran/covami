package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	@Before
	static void addDefaults() {
		renderArgs.put("covamiTitle",
				Play.configuration.getProperty("covami.title"));
		renderArgs.put("covamiBaseline",
				Play.configuration.getProperty("covami.baseline"));
	}

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			Utilisateur user = Utilisateur
					.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user);
			renderArgs.put("security", Security.connected());
			flash.success("Connexion réussie");
		}
	}

	public static void index() {
		render();
	}

}