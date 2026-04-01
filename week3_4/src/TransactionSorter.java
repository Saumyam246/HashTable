package Week3_4;

import java.util.*;

public class TransactionSorter {

    // 🔹 Transaction class (inner class)
    static class Transaction {
        String id;
        double fee;
        String timestamp;

        public Transaction(String id, double fee, String timestamp) {
            this.id = id;
            this.fee = fee;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return id + ":" + fee + "@" + timestamp;
        }
    }

    // 🔹 Bubble Sort (by fee ascending)
    public static void bubbleSortByFee(List<Transaction> list) {
        int n = list.size();
        int passes = 0, swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            passes++;

            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Collections.swap(list, j, j + 1);
                    swaps++;
                    swapped = true;
                }
            }

            // Early stop if already sorted
            if (!swapped) break;
        }

        System.out.println("Bubble Sort Result: " + list);
        System.out.println("Passes: " + passes + ", Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (by fee + timestamp)
    public static void insertionSortByFeeAndTime(List<Transaction> list) {
        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j)); // shift right
                j--;
            }

            list.set(j + 1, key);
        }

        System.out.println("Insertion Sort Result: " + list);
    }

    // 🔹 Compare fee, then timestamp
    private static int compare(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee) {
            return Double.compare(t1.fee, t2.fee);
        }
        return t1.timestamp.compareTo(t2.timestamp);
    }

    // 🔹 High-fee outliers (> 50)
    public static void findHighFeeOutliers(List<Transaction> list) {
        boolean found = false;

        for (Transaction t : list) {
            if (t.fee > 50) {
                if (!found) {
                    System.out.print("High-fee outliers: ");
                    found = true;
                }
                System.out.print(t + " ");
            }
        }

        if (!found) {
            System.out.println("High-fee outliers: none");
        } else {
            System.out.println();
        }
    }

    // 🔹 Main method
    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));

        // Copy lists
        List<Transaction> bubbleList = new ArrayList<>(transactions);
        List<Transaction> insertionList = new ArrayList<>(transactions);

        // 🔸 Bubble Sort
        bubbleSortByFee(bubbleList);

        // 🔸 Insertion Sort
        insertionSortByFeeAndTime(insertionList);

        // 🔸 Outliers
        findHighFeeOutliers(transactions);
    }
}