package algorithms.m3;

import model.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Bitmask Dynamic Programming for Activity Schedule Optimization.
 *
 * Evaluates all 2^N activity subsets using integer bitmasks to select the optimal
 * combination of activities that maximizes total priority value without exceeding maximum available time.
 *
 * Time Complexity: O(2^N)
 * Space Complexity: O(2^N)
 */
public class BitmaskDP {

    /**
     * Result container for Bitmask DP optimization.
     */
    public static class OptimizationResult {
        public List<Activity> selectedActivities;
        public int totalDuration;
        public int totalValue;
        public int bestMask;
        public int totalStatesEvaluated;

        public OptimizationResult(List<Activity> selectedActivities, int totalDuration, int totalValue, int bestMask, int totalStatesEvaluated) {
            this.selectedActivities = selectedActivities;
            this.totalDuration = totalDuration;
            this.totalValue = totalValue;
            this.bestMask = bestMask;
            this.totalStatesEvaluated = totalStatesEvaluated;
        }
    }

    /**
     * Optimizes activity schedule using Bitmask Dynamic Programming.
     * Finds the subset of activities with total duration <= maxTime that maximizes total value.
     *
     * @param activities List of available activities (up to 20 items for Bitmask DP)
     * @param maxTime Maximum available time constraint (in minutes)
     * @return OptimizationResult containing selected activities, total duration, total value, and DP stats.
     */
    public static OptimizationResult optimize(List<Activity> activities, int maxTime) {
        if (activities == null || activities.isEmpty() || maxTime <= 0) {
            return new OptimizationResult(new ArrayList<>(), 0, 0, 0, 0);
        }

        // Limit N to 20 to prevent integer overflow in bit shift (1 << N)
        int n = Math.min(activities.size(), 20);
        int numStates = 1 << n;

        int[] dpTime = new int[numStates];
        int[] dpVal = new int[numStates];

        dpTime[0] = 0;
        dpVal[0] = 0;

        int bestMask = 0;
        int maxVal = 0;
        int bestTime = 0;

        // Dynamic Programming transitions over bitmask subsets
        for (int mask = 1; mask < numStates; mask++) {
            // Find lowest set bit in mask
            int i = Integer.numberOfTrailingZeros(mask);
            int prevMask = mask ^ (1 << i);

            Activity act = activities.get(i);
            dpTime[mask] = dpTime[prevMask] + act.getDurationMinutes();
            dpVal[mask] = dpVal[prevMask] + act.getValue();

            // Evaluate state validity under maxTime constraint
            if (dpTime[mask] <= maxTime) {
                if (dpVal[mask] > maxVal || (dpVal[mask] == maxVal && dpTime[mask] < bestTime)) {
                    maxVal = dpVal[mask];
                    bestTime = dpTime[mask];
                    bestMask = mask;
                }
            }
        }

        // Reconstruct selected activities from bestMask
        List<Activity> selected = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                selected.add(activities.get(i));
            }
        }

        return new OptimizationResult(selected, bestTime, maxVal, bestMask, numStates);
    }
}
