package synthesizer;


public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int capacity;    // size of the buffer
    protected int fillCount;   // number of items currently in the buffer

    public AbstractBoundedQueue(int capacity) {
        this.capacity = capacity;
        this.fillCount = 0;
    }

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
