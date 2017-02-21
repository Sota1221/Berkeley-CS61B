// Make sure to make this class a part of the synthesizer package
// package <package>
package synthesizer;
import javax.swing.text.html.HTMLDocument;
//import edu.princeton.cs.algs4.StdDraw;
import java.util.Iterator;


// Make sure to make this class and all of its methods public
// Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        //       Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        this.capacity = capacity;
        this.fillCount = 0;
        this.rb = (T[]) new Object[this.capacity];
        this.first = 0;
        this.last = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        //  Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        this.rb[last] = x;
        this.fillCount += 1;
        if (this.last == this.capacity - 1) {
            this.last = 0;
            return;
        }
        this.last += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        // Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T oldestItem = this.rb[first];
        this.rb[first] = null;
        this.fillCount -= 1;
        if (isEmpty()) {
            this.first = 0;
            this.last = 0;
            return oldestItem;
        }
        if (this.first == this.capacity - 1) {
            this.first = 0;
            return oldestItem;
        }
        this.first += 1;
        return oldestItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        // Return the first item. None of your instance variables should change.
        return this.rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator<T>(this);
    }


    // When you get to part 5, implement the needed code to support iteration.
    private class ArrayRingBufferIterator<T> implements Iterator<T> {
        private ArrayRingBuffer<T> pointer;
        private int counter;

        ArrayRingBufferIterator(ArrayRingBuffer<T> p) {
            this.pointer = p;
            this.counter = 0;
        }

        public boolean hasNext() {
            if (this.counter == this.pointer.capacity) {
                return false;
            }
            return true;
        }

        public T next() {
            int nextIndex = this.pointer.first + counter;
            this.counter += 1;
            return this.pointer.rb[nextIndex % this.pointer.capacity];
        }
    }
}
