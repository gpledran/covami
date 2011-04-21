package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

public class Troncon extends Model {

	@Required
	public String nomAutoroute;

	@Required
	public String villeSuivante;

	@Required
	public int tarif;

	@Required
	public int nbKms;
}
