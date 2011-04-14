package controllers;
 
import play.*;
import play.mvc.*;
 
import java.util.*;
 
import models.*;
 
@With(Secure.class)
public class Admin extends Controller {
    
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            Utilisateur user = Utilisateur.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user);
        }
    }
 
    public static void index() {
        render();
    }
    
}