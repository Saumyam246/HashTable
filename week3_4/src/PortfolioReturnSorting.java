import java.util.*;

public class PortfolioReturnSorting {

    // 🔹 Asset class
    static class Asset {
        String name;
        double returnRate;
        double volatility;

        public Asset(String name, double returnRate, double volatility) {
            this.name = name;
            this.returnRate = returnRate;
            this.volatility = volatility;
        }

        @Override
        public String toString() {
            return name + ":" + returnRate + "%";
        }
    }

    // 🔹 Merge Sort (stable, ASC by returnRate)
    public static void mergeSort(Asset[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(Asset[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Asset[] L = new Asset[n1];
        Asset[] R = new Asset[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i].returnRate <= R[j].returnRate) { // stable
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // 🔹 Quick Sort (DESC returnRate + ASC volatility)
    public static void quickSort(Asset[] arr, int low, int high) {
        if (low < high) {
            int pi = medianOfThreePartition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // 🔹 Median-of-3 Pivot Selection
    private static int medianOfThreePartition(Asset[] arr, int low, int high) {
        int mid = (low + high) / 2;

        // Sort low, mid, high
        if (compare(arr[low], arr[mid]) > 0) swap(arr, low, mid);
        if (compare(arr[low], arr[high]) > 0) swap(arr, low, high);
        if (compare(arr[mid], arr[high]) > 0) swap(arr, mid, high);

        // Use mid as pivot → move to end
        swap(arr, mid, high);
        return partition(arr, low, high);
    }

    // 🔹 Partition (DESC return + ASC volatility)
    private static int partition(Asset[] arr, int low, int high) {
        Asset pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(arr[j], pivot) < 0) { // better asset first
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    // 🔹 Comparator: DESC return, ASC volatility
    private static int compare(Asset a, Asset b) {
        if (a.returnRate != b.returnRate) {
            return Double.compare(b.returnRate, a.returnRate); // DESC
        }
        return Double.compare(a.volatility, b.volatility); // ASC
    }

    // 🔹 Swap helper
    private static void swap(Asset[] arr, int i, int j) {
        Asset temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 🔹 Main method
    public static void main(String[] args) {

        Asset[] assets = {
                new Asset("AAPL", 12, 0.3),
                new Asset("TSLA", 8, 0.6),
                new Asset("GOOG", 15, 0.2)
        };

        // Copy arrays
        Asset[] mergeArray = Arrays.copyOf(assets, assets.length);
        Asset[] quickArray = Arrays.copyOf(assets, assets.length);

        // 🔸 Merge Sort (ASC)
        mergeSort(mergeArray, 0, mergeArray.length - 1);
        System.out.println("Merge (asc): " + Arrays.toString(mergeArray));

        // 🔸 Quick Sort (DESC)
        quickSort(quickArray, 0, quickArray.length - 1);
        System.out.println("Quick (desc): " + Arrays.toString(quickArray));
    }
}