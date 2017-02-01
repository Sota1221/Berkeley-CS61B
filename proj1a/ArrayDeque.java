public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int firstIndex;
    private int rearIndex;
    private int currentCap = 8;

    private static int RFACTOR = 2;

    public ArrayDeque() {
        items = (Item[]) new Object[currentCap];
        size = 0;
    }

    private void resize(boolean flag, int capacity, int currPos,
                        int nextPos, int len, Item[] prevPlace, Item[] nextPlace) {
        if (flag) {
            Item[] a = (Item[]) new Object[capacity];
            System.arraycopy(prevPlace, currPos, a, nextPos, len);
            items = a;
            currentCap = capacity;
        } else {
            System.arraycopy(prevPlace, currPos, nextPlace, nextPos, len);
        }
    }

    public void addFirst(Item item) {
        if (isEmpty()) {
            items[0] = item;
            firstIndex = 0;
            rearIndex = 0;
            size += 1;
        } else if (firstIndex != 0) {
            if (currentCap == size) {
                Item[] temp = items;
                resize(true, size * RFACTOR, firstIndex,
                        1, items.length - firstIndex, items, null);
                resize(false, currentCap, 0,
                        temp.length - firstIndex + 1, rearIndex + 1, temp, items);
                firstIndex = 0;
                items[firstIndex] = item;
                rearIndex = size;
                size += 1;
            } else {
                items[firstIndex - 1] = item;
                firstIndex -= 1;
                size += 1;
            }
        } else {
            if (currentCap == size) {
                resize(true, size * RFACTOR, 0, 0, items.length, items, null);
                rearIndex = size;
                size += 1;
            } else {
                items[items.length - 1] = item;
                firstIndex = items.length - 1;
                size += 1;
            }
        }
    }

    public void addLast(Item item) {
        if (isEmpty()) {
            items[0] = item;
            firstIndex = 0;
            rearIndex = 0;
            size += 1;
        } else if (firstIndex != 0) {
            if (currentCap == size) {
                Item[] temp = items;
                resize(true, size * RFACTOR, firstIndex,
                        0, items.length - firstIndex, items, null);
                resize(false, currentCap, 0,
                        temp.length - firstIndex, rearIndex + 1, temp, items);
                firstIndex = 0;
                rearIndex = size;
                items[rearIndex] = item;
                size += 1;
            } else {
                rearIndex = (rearIndex + 1) % items.length;
                items[rearIndex] = item;
                size += 1;
            }
        } else {
            if (currentCap == size) {
                resize(true, size * RFACTOR, 0, 0, items.length, items, null);
                rearIndex = size;
                items[rearIndex] = item;
                size += 1;
            } else {
                rearIndex += 1;
                items[rearIndex] = item;
                size += 1;
            }
        }
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        if (firstIndex == rearIndex) {
            System.out.print(items[firstIndex]);
        } else {
            int i = firstIndex;
            while (i != rearIndex) {
                System.out.print(items[i] + " ");
                i += 1;
                if (i == items.length) {
                    i = 0;
                }
            }
        }
    }

    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item first = items[firstIndex];
        items[firstIndex] = null;
        if (size == 1) {
            firstIndex = -1;
            rearIndex = -1;
        } else if (firstIndex == items.length - 1) {
            firstIndex = 0;
        } else {
            firstIndex += 1;
        }
        size -= 1;
        return first;
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item last = items[rearIndex];
        items[rearIndex] = null;
        if (size == 0) {
            firstIndex = -1;
            rearIndex = -1;
        } else if (rearIndex == 0) {
            rearIndex = items.length - 1;
        } else {
            rearIndex -= 1;
        }
        size -= 1;
        return last;
    }

    public Item get(int index) {
        if (isEmpty()) {
            return null;
        }
        return items[(firstIndex + index) % items.length];
    }
}

