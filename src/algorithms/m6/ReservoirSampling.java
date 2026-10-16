package algorithms.m6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Reservoir Sampling Algorithm (Algorithm R — Vitter 1985).
 *
 * Draws a uniformly random sample of exactly k items from a data stream
 * without loading the entire stream into memory first.
 *
 * Algorithm:
 *   1. Fill the reservoir with the first k elements.
 *   2. For each subsequent element at stream position i (1-based, i > k):
 *        a. Generate a random integer j in [1, i].
 *        b. If j <= k, replace reservoir[j-1] with the current element.
 *
 * Guarantee: Every element in the full stream has exactly k/n probability
 * of being in the final reservoir sample (uniform distribution).
 *
 * Time Complexity: O(N) — processes stream in one pass.
 * Space Complexity: O(k) — only the reservoir is held in memory.
 */
public class ReservoirSampling {

    private static final Random RNG = new Random();

    /**
     * Result container for a Reservoir Sampling run.
     */
    public static class SamplingResult {
        public final List<String> sample;   // Sampled item labels
        public final int streamSize;
        public final int reservoirSize;     // Requested k
        public final int actualSampleSize;  // min(k, streamSize)

        public SamplingResult(List<String> sample, int streamSize, int reservoirSize) {
            this.sample = sample;
            this.streamSize = streamSize;
            this.reservoirSize = reservoirSize;
            this.actualSampleSize = sample.size();
        }
    }

    /**
     * Performs Reservoir Sampling over a stream of String items.
     *
     * Handles edge cases:
     *   - Empty stream → returns empty sample.
     *   - k <= 0       → returns empty sample.
     *   - k >= stream  → returns entire stream (all elements qualify).
     *
     * @param stream List of items representing the data stream (processed sequentially).
     * @param k      Desired reservoir / sample size.
     * @return SamplingResult containing the random sample and metadata.
     */
    public static SamplingResult sample(List<String> stream, int k) {
        List<String> reservoir = new ArrayList<>();

        if (stream == null || stream.isEmpty() || k <= 0) {
            return new SamplingResult(reservoir, stream == null ? 0 : stream.size(), k);
        }

        int n = stream.size();

        // Step 1: Fill reservoir with first k elements
        int fillSize = Math.min(k, n);
        for (int i = 0; i < fillSize; i++) {
            reservoir.add(stream.get(i));
        }

        // Step 2: Process remaining elements (positions k to n-1)
        // For stream position i (0-based), the 1-based index is i+1.
        // Generate j in [0, i] and replace reservoir[j] if j < k.
        for (int i = k; i < n; i++) {
            int j = RNG.nextInt(i + 1); // random in [0, i]
            if (j < k) {
                reservoir.set(j, stream.get(i));
            }
        }

        return new SamplingResult(reservoir, n, k);
    }

    /**
     * Convenience overload: samples from a stream of generic SortRecord objects,
     * returning a list of the sampled SortRecords.
     *
     * @param stream List of SortRecord objects (event analytics records).
     * @param k      Desired sample size.
     * @return List of sampled SortRecord objects (size = min(k, stream.size())).
     */
    public static List<RandomizedQuickSort.SortRecord> sampleRecords(
            List<RandomizedQuickSort.SortRecord> stream, int k) {

        List<RandomizedQuickSort.SortRecord> reservoir = new ArrayList<>();

        if (stream == null || stream.isEmpty() || k <= 0) {
            return reservoir;
        }

        int n = stream.size();
        int fillSize = Math.min(k, n);

        for (int i = 0; i < fillSize; i++) {
            reservoir.add(stream.get(i));
        }

        for (int i = k; i < n; i++) {
            int j = RNG.nextInt(i + 1);
            if (j < k) {
                reservoir.set(j, stream.get(i));
            }
        }

        return reservoir;
    }
}
