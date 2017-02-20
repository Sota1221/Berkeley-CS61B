package synthesizer;


public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int capacity;    // size of the buffer
    protected int fillCount;   // number of items currently in the buffer

    // return size of the buffer
    @Override
    public int capacity() {
        return this.capacity;
    }

    // return number of items currently in the buffer
    @Override
    public int fillCount() {
        return this.fillCount;
    }
}
