public class LinkedListDeque<Item> {
    private Node sentinel;
    private int size;

    private class Node {
        private Node prev;
        private Item item;
        private Node next;

        public Node(Node prev0, Item item0, Node next0) {
            prev = prev0;
            item = item0;
            next = next0;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    private Item helper(int index, Node node) {
        if (index == 0) {
            return node.item;
        }
        return helper(index - 1, node.next);
    }

    public Item getRecursive(int index) {
        if (index == 0) {
            return sentinel.next.item;
        }
        return helper(index - 1, sentinel.next.next);
    }

    public void addFirst(Item item) {
        if (isEmpty()) {
            Node newFrontNode = new Node(sentinel, item, sentinel);
            sentinel.next = newFrontNode;
            sentinel.prev = newFrontNode;
            size += 1;
        } else {
            Node newFrontNode = new Node(sentinel, item, sentinel.next);
            sentinel.next = newFrontNode;
            newFrontNode.next.prev = newFrontNode;
            size += 1;
        }
    }

    public void addLast(Item item) {
        if (isEmpty()) {
            Node newFrontNode = new Node(sentinel, item, sentinel);
            sentinel.next = newFrontNode;
            sentinel.prev = newFrontNode;
            size += 1;
        } else {
            Node newFrontNode = new Node(sentinel.prev, item, sentinel);
            sentinel.prev.next = newFrontNode;
            sentinel.prev = newFrontNode;
            size += 1;
        }
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node pointer = sentinel.next;
        while (pointer.next != sentinel) {
            System.out.print(pointer.item + " ");
            pointer = pointer.next;
        }
        System.out.print(pointer.item);
    }

    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node pointer = sentinel.next;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return pointer.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node pointer = sentinel.prev;
        Node last = pointer.prev;
        last.next = sentinel;
        sentinel.prev = last;
        size -= 1;
        return pointer.item;
    }

    public Item get(int index) {
        if (isEmpty()) {
            return null;
        } else {
            Node pointer = sentinel.next;
            while (index != 0) {
                pointer = pointer.next;
                index -= 1;
            }
            return pointer.item;
        }
    }
}
