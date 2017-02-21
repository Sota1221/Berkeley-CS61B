package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(10);
        for (int i = 0; i < arb.capacity(); i++) {
            arb.enqueue(new Integer(i));
        }
        for (int k = 0; k < arb.capacity() - 1; k++) {
            assertEquals(new Integer(k), arb.dequeue());
        }
        arb.enqueue(new Integer(10));
        arb.dequeue();
        assertEquals(new Integer(10), arb.peek());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
