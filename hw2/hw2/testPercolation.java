package hw2;


import edu.princeton.cs.introcs.StdAudio;
import org.junit.Test;
import static org.junit.Assert.*;


/* Since this test is part of a package, we have to import the package version of StdAudio. */
/* Don't worry too much about this, we'll get there in due time. */


/** Tests the GuitarString class.
 *  @author Josh Hug
 */

public class testPercolation {
    @Test
    public void testPluckTheAString() {
        Percolation test = new Percolation(1);
        assertTrue(!test.percolates());
        try {
            test.open(1,1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("OK");
        }
        test.open(0,0);
        assertTrue(test.percolates());
    }



    /** Calls tests for GuitarString. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(Percolation.class);
    }
}