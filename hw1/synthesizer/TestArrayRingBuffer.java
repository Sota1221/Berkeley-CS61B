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
        arb.enqueue(new Integer(3));
        arb.enqueue(new Integer(1));
        arb.enqueue(new Integer(9));
        assertEquals(new Integer(3), arb.peek());
        arb.dequeue();
        assertEquals(new Integer(1), arb.dequeue());
        assertEquals(new Integer(9), arb.peek());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
