/**
 * Created by sota_ on 2/4/2017.
 */
import static org.junit.Assert.*;
import org.junit.Test;


public class TestArrayDeque1B {

    private static int numberOfTrials = 20;
    private static double probabilityOfRemoving = 0.3;
    private static double probabilityForFirst = 0.5;
    private int currentSize = 0;

    @Test
    public void arrayDequeTest() {
        /** Cited from StudentArrayDequeLauncher. */
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        OperationSequence process = new OperationSequence();

        while (true) {
            double numberBetweenZeroAndOne1 = StdRandom.uniform();
            double numberBetweenZeroAndOne2 = StdRandom.uniform();
            Integer randomInt = StdRandom.uniform(0, 10);
            if (currentSize > 0
                    && numberBetweenZeroAndOne1 < probabilityOfRemoving) {
                currentSize -= 1;
                if (numberBetweenZeroAndOne2 < probabilityForFirst) {
                    DequeOperation fs = new DequeOperation("removeLast");
                    process.addOperation(fs);
                    Integer lastSad1 = sad1.removeLast();
                    Integer lastAds1 = ads1.removeLast();
                    assertEquals(process.toString(),
                            lastAds1, lastSad1);
                } else {
                    DequeOperation fs = new DequeOperation("removeFirst");
                    process.addOperation(fs);
                    Integer firstSad1 = sad1.removeFirst();
                    Integer firstAds1 = ads1.removeFirst();
                    assertEquals(process.toString(),
                            firstAds1, firstSad1);
                }
            } else {
                currentSize += 1;
                if (numberBetweenZeroAndOne2 < probabilityForFirst) {
                    DequeOperation fs = new DequeOperation("addLast", randomInt);
                    process.addOperation(fs);
                    sad1.addLast(randomInt);
                    ads1.addLast(randomInt);
                } else {
                    DequeOperation fs = new DequeOperation("addFirst", randomInt);
                    process.addOperation(fs);
                    sad1.addFirst(randomInt);
                    ads1.addFirst(randomInt);
                }
            }
        }
    }

    public static void main(String... args) {
        jh61b.junit.TestRunner.runTests("all", TestArrayDeque1B.class);
    }
}


