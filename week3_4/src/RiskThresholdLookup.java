import java.util.*;

public class RiskThresholdLookup {

    // 🔹 Linear Search (unsorted)
    public static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                System.out.println("Linear: Found at index " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Linear: Not found (" + comparisons + " comparisons)");
        }
    }

    // 🔹 Binary Search: find floor & ceiling
    public static void binarySearchFloorCeil(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;

        Integer floor = null;
        Integer ceil = null;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] == target) {
                floor = arr[mid];
                ceil = arr[mid];
                break;
            } else if (arr[mid] < target) {
                floor = arr[mid]; // possible floor
                low = mid + 1;
            } else {
                ceil = arr[mid]; // possible ceiling
                high = mid - 1;
            }
        }

        System.out.println("Binary Search:");
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Floor(" + target + "): " + (floor != null ? floor : "none"));
        System.out.println("Ceiling(" + target + "): " + (ceil != null ? ceil : "none"));
    }

    // 🔹 Find insertion point (lower_bound)
    public static int findInsertionPoint(int[] arr, int target) {
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = (low + high) / 2;

            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low; // position to insert
    }

    // 🔹 Main method
    public static void main(String[] args) {

        int[] unsorted = {50, 10, 100, 25};
        int[] sorted = {10, 25, 50, 100};

        int target = 30;

        // 🔸 Linear Search (unsorted)
        linearSearch(unsorted, target);

        // 🔸 Binary Search (sorted)
        binarySearchFloorCeil(sorted, target);

        // 🔸 Insertion Point
        int index = findInsertionPoint(sorted, target);
        System.out.println("Insertion point for " + target + ": index " + index);
    }
}