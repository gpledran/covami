import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
	@Before
	public void creerUtilisateur() {
		// Create a new user and save it
        //new Utilisateur("gpledran@gmail.com", "azerty", "Pledran", "Gaudéric", "0632375995", "0950024506", new Date(1987-06-02), "14 Rue Michel Columb", "44200", "Nantes", "France").save();
	}

    @Test
    public void retrouverUtilisateur() {
        // Rechercher utilisateur par login
        Utilisateur gpledran = Utilisateur.find("byLogin", "gpledran").first();
        
        // Test 
        assertNotNull(gpledran);
        assertEquals("gpledran@gmail.com", gpledran.email);
    }
    
    @Test
    public void connecterUtilisateur() {
        // Test 
        assertNotNull(Utilisateur.connect("gpledran", "azerty"));
        assertNull(Utilisateur.connect("gpledran", "badpassword"));
        assertNull(Utilisateur.connect("badlogin", "azerty"));
    }

}
