/**
 * Created by sota_ on 2/2/2017.
 */

import static org.junit.Assert.*;
import org.junit.Test;


public class FlikTest {
    @Test
    public void testIsSameNumber() {
        Integer a = 5;
        Integer b = 5;
        Integer c = 200;
        Integer x = 200;
        int d = 500;
        int e = 500;
        int f = 200;
//        System.out.println(a == b);
//        System.out.println(c == x);
        System.out.println(Flik.isSameNumber(a, b));
        System.out.println(Flik.isSameNumber(b, c));
        System.out.println(Flik.isSameNumber(x, c));

        System.out.println(Flik.isSameNumber(d, e));
//        HorribleSteve.main();
        assertTrue(!(Flik.isSameNumber(a, b)));
        assertTrue(Flik.isSameNumber(b, c));
    }



    public static void main(String... args) {jh61b.junit.TestRunner.runTests("all", Flik.class); }
}
