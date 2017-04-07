package hw4.hash;

import java.util.List;
import java.util.LinkedList;


public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */

        System.out.println("M:" + M);
        LinkedList<Oomage>[] buckets = new LinkedList[M];
        int n = oomages.size();
        for (int k = 0; k < M; k++) {
            buckets[k] = new LinkedList<>();
        }
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum].add(o);
        }

        for (int i = 0; i < M; i++) {
            if (buckets[i].size() < n / 50 || buckets[i].size() > n / 2.5) {
                return false;
            }
        }
        return true;
    }
}
