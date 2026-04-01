import java.util.*;

public class AccountIdLookup {

    // 🔹 Linear Search: first & last occurrence
    public static void linearSearch(String[] arr, String target) {
        int first = -1, last = -1;
        int comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        System.out.println("Linear Search:");
        if (first == -1) {
            System.out.println("Not found (" + comparisons + " comparisons)");
        } else {
            System.out.println("First index: " + first + ", Last index: " + last);
            System.out.println("Comparisons: " + comparisons);
        }
    }

    // 🔹 Binary Search: find one occurrence
    public static int binarySearch(String[] arr, String target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            int cmp = arr[mid].compareTo(target);

            if (cmp == 0) {
                System.out.println("Binary Search Comparisons: " + comparisons);
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("Binary Search Comparisons: " + comparisons);
        return -1;
    }

    // 🔹 Count occurrences using binary search expansion
    public static int countOccurrences(String[] arr, String target, int index) {
        if (index == -1) return 0;

        int count = 1;

        // left side
        int i = index - 1;
        while (i >= 0 && arr[i].equals(target)) {
            count++;
            i--;
        }

        // right side
        i = index + 1;
        while (i < arr.length && arr[i].equals(target)) {
            count++;
            i++;
        }

        return count;
    }

    // 🔹 Main method
    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};

        String target = "accB";

        // 🔸 Linear Search (unsorted)
        linearSearch(logs, target);

        // 🔸 Sort for Binary Search
        Arrays.sort(logs);
        System.out.println("\nSorted Logs: " + Arrays.toString(logs));

        // 🔸 Binary Search
        int index = binarySearch(logs, target);

        if (index != -1) {
            int count = countOccurrences(logs, target, index);
            System.out.println("Binary found at index: " + index + ", count=" + count);
        } else {
            System.out.println("Target not found");
        }
    }
}