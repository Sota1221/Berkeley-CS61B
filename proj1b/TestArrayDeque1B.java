/**
 * Created by sota_ on 2/4/2017.
 */
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.concurrent.DelayQueue;

public class TestArrayDeque1B {


    @Test
    public void ArrayDequeTest() {
        /** Cited from StudentArrayDequeLauncher. */
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        OperationSequence process = new OperationSequence();


        for (int i = 0; i < 20; i += 1) {
            double numberBetweenZeroAndOne1 = StdRandom.uniform();
            double numberBetweenZeroAndOne2 = StdRandom.uniform();
            int randomInt = StdRandom.uniform(0, 10);
            double randomNumber = StdRandom.uniform();

            if (ads1.size() > 0 && sad1.size() > 0 && numberBetweenZeroAndOne1 < 0.3) {
                if (numberBetweenZeroAndOne2 < 0.5) {
                    DequeOperation fs = new DequeOperation("removeLast");
                    process.addOperation(fs);
                    int lastSad1 = sad1.removeLast();
                    int lastAds1 = ads1.removeLast();
                    assertEquals(process.toString(),
                            lastAds1, lastSad1);
                } else {
                    DequeOperation fs = new DequeOperation("removeFirst");
                    process.addOperation(fs);
                    int firstSad1 = sad1.removeFirst();
                    int firstAds1 = ads1.removeFirst();
                    assertEquals(process.toString(),
                            firstAds1, firstSad1);
                }
            } else {
                if (numberBetweenZeroAndOne2 < 0.5) {
                    DequeOperation fs = new DequeOperation("addFirst", randomInt);
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
        while (sad1.size() > 0 && ads1.size() > 0) {
            double randomNumber3 = StdRandom.uniform();

            if (randomNumber3 < 0.5) {
                DequeOperation fs = new DequeOperation("removeLast");
                process.addOperation(fs);
                int lastSad1 = sad1.removeLast();
                int lastAds1 = ads1.removeLast();
                assertEquals(process.toString(),
                        lastAds1, lastSad1);
            } else {
                DequeOperation fs = new DequeOperation("removeFirst");
                process.addOperation(fs);
                int firstSad1 = sad1.removeFirst();
                int firstAds1 = ads1.removeFirst();
                assertEquals(process.toString(),
                        firstAds1, firstSad1);
            }
        }
    }
}


