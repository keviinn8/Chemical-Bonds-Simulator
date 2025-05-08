import com.example.chemistryproj.Atom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AtomTest {

    @Test
    void testGetNumberOfValenceElectrons() {
        assertEquals(1, Atom.getNumberOfValenceElectrons("H"));
        assertEquals(2, Atom.getNumberOfValenceElectrons("Be"));
        assertEquals(5, Atom.getNumberOfValenceElectrons("N"));
        assertEquals(9, Atom.getNumberOfValenceElectrons("He"));
        assertEquals(0, Atom.getNumberOfValenceElectrons("X"));
    }
    
}
