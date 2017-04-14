import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> singleItemQueues = new Queue<>();
        for (Item e: items) {
            Queue<Item> q = new Queue<Item>();
            q.enqueue(e);
            singleItemQueues.enqueue(q);
        }
        return singleItemQueues;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> mergeSortedQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            mergeSortedQueue.enqueue(getMin(q1, q2));
        }
        return mergeSortedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        Queue<Queue<Item>> singles = makeSingleItemQueues(items);
        while (singles.size() != 1) {
            for (int i = 0; i < singles.size() / 2; i++) {
                singles.enqueue(mergeSortedQueues(
                        singles.dequeue(), singles.dequeue()));
            }
            if (singles.size() % 2 == 1) {
                singles.enqueue(singles.dequeue());
            }
        }
        return singles.dequeue();
    }




    // write a lightweight test
    public static void main(String[] args) {
        Queue<String> students = new Queue<>();
        students.enqueue("Sota");
        students.enqueue("Yuki");
        students.enqueue("Daigo");
        students.enqueue("Koichi");
        students.enqueue("Harumi");
        students.enqueue("Yoshikazu");
        students.enqueue("Yukiko");
        students.enqueue("Kazuko");
        students.enqueue("Masaji");
        // print unsorted queue
        for (String e: students) {
            System.out.println(e);
        }
        // create deep copy
        Queue<String> studentCopy  = new Queue<>();
        for (String e: students) {
            studentCopy.enqueue(e);
        }
        System.out.println("");
        students = MergeSort.mergeSort(students);
        for (String e: students) {
            System.out.println(e);
        }
        System.out.println("");
        for (String e: studentCopy) {
            System.out.println(e);
        }
    }
}