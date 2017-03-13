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

        Percolation test2 = new Percolation(3);
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                test2.open(i, k);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                assertTrue(test2.isOpen(i, k));
                assertTrue(test2.isFull(i, k));
            }
        }

        Percolation test3 = new Percolation(10);
        for (int j = 0; j < 10; j++) {
            test3.open(j, 5);
        }
        assertTrue(test3.percolates());
    }



    /** Calls tests for GuitarString. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(Percolation.class);
    }
}