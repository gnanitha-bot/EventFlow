package algorithms.m6;

import java.util.Random;

/**
 * Randomized QuickSort Algorithm.
 *
 * Selects a RANDOM pivot at each recursive step to achieve expected O(N log N) performance.
 * Partitions the array in-place around the pivot, then recursively sorts both halves.
 *
 * Does NOT delegate to Arrays.sort() or Collections.sort().
 *
 * Time Complexity: Expected O(N log N) | Worst Case O(N^2) [extremely unlikely with random pivot]
 * Space Complexity: O(log N) average stack depth
 */
public class RandomizedQuickSort {

    private static final Random RNG = new Random();

    /**
     * Sorts a double[] array in ascending order using Randomized QuickSort.
     * Pass a copy if you need to preserve the original.
     *
     * @param arr    Array to sort (sorted in-place)
     * @param left   Left bound (inclusive)
     * @param right  Right bound (inclusive)
     */
    public static void sort(double[] arr, int left, int right) {
        if (arr == null || left >= right) return;

        int pivotIdx = partition(arr, left, right);
        sort(arr, left, pivotIdx - 1);
        sort(arr, pivotIdx + 1, right);
    }

    /**
     * Sorts an array of {@link SortRecord} objects in-place by their {@code value} field.
     *
     * @param arr       Array of SortRecord to sort (in-place)
     * @param left      Left bound (inclusive)
     * @param right     Right bound (inclusive)
     * @param ascending true = ascending order, false = descending order
     */
    public static void sort(SortRecord[] arr, int left, int right, boolean ascending) {
        if (arr == null || left >= right) return;

        int pivotIdx = partition(arr, left, right, ascending);
        sort(arr, left, pivotIdx - 1, ascending);
        sort(arr, pivotIdx + 1, right, ascending);
    }

    /**
     * Partitions a double[] sub-array around a randomly chosen pivot.
     * Elements less than pivot go left; elements greater go right.
     *
     * @return Final position of the pivot after partition
     */
    private static int partition(double[] arr, int left, int right) {
        // Choose a random pivot index and swap it to the rightmost position
        int randomPivotIdx = left + RNG.nextInt(right - left + 1);
        swap(arr, randomPivotIdx, right);

        double pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, right);
        return i + 1;
    }

    /**
     * Partitions a SortRecord[] sub-array around a randomly chosen pivot.
     */
    private static int partition(SortRecord[] arr, int left, int right, boolean ascending) {
        int randomPivotIdx = left + RNG.nextInt(right - left + 1);
        swap(arr, randomPivotIdx, right);

        double pivot = arr[right].value;
        int i = left - 1;

        for (int j = left; j < right; j++) {
            boolean condition = ascending ? arr[j].value <= pivot : arr[j].value >= pivot;
            if (condition) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, right);
        return i + 1;
    }

    private static void swap(double[] arr, int i, int j) {
        double tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }

    private static void swap(SortRecord[] arr, int i, int j) {
        SortRecord tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }

    /**
     * Generic record pairing a label (event name, ID) with a numeric sort value.
     */
    public static class SortRecord {
        public String id;
        public String label;
        public double value;
        public String unit;

        public SortRecord(String id, String label, double value, String unit) {
            this.id = id;
            this.label = label;
            this.value = value;
            this.unit = unit;
        }
    }
}
