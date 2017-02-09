/**
 * Created by sota_ on 2/4/2017.
 */
import static org.junit.Assert.*;
import org.junit.Test;


public class TestArrayDeque1B {

    private final int numberOfTrials = 20;
    private final double probabilityOfRemoving = 0.3;
    private final double probabilityForFirst = 0.5;
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
                    process.addOperation(new DequeOperation("removeLast"));
                    assertEquals(process.toString(),
                            ads1.removeLast(), sad1.removeLast());
                } else {
                    process.addOperation(new DequeOperation("removeFirst"));
                    assertEquals(process.toString(),
                            ads1.removeFirst(), sad1.removeFirst());
                }
            } else {
                currentSize += 1;
                if (numberBetweenZeroAndOne2 < probabilityForFirst) {
                    process.addOperation(new DequeOperation("addLast", randomInt));
                    sad1.addLast(randomInt);
                    ads1.addLast(randomInt);
                } else {
                    process.addOperation(new DequeOperation("addFirst", randomInt));
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


