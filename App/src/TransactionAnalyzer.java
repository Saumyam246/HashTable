import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println("Two-Sum Match → (" +
                        other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two Sum within 1 hour window
    public void findTwoSumWithTime(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= 3600) {

                    System.out.println("Two-Sum (1h window) → (" +
                            other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.println("Duplicate Payment Detected → " + key);

                for (Transaction t : list) {
                    System.out.println("Transaction ID: " + t.id +
                            " Account: " + t.account);
                }
            }
        }
    }

    // K-Sum (recursive)
    public void findKSum(int k, int target) {
        kSumHelper(new ArrayList<>(), 0, k, target);
    }

    private void kSumHelper(List<Transaction> path, int start, int k, int target) {

        if (k == 0 && target == 0) {

            System.out.print("K-Sum Match → ");
            for (Transaction t : path)
                System.out.print(t.id + " ");

            System.out.println();
            return;
        }

        if (k == 0 || start >= transactions.size())
            return;

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            path.add(t);

            kSumHelper(path, i + 1, k - 1, target - t.amount);

            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        long baseTime = System.currentTimeMillis() / 1000;

        analyzer.addTransaction(new Transaction(1, 500, "Store A", "acc1", baseTime));
        analyzer.addTransaction(new Transaction(2, 300, "Store B", "acc2", baseTime + 900));
        analyzer.addTransaction(new Transaction(3, 200, "Store C", "acc3", baseTime + 1800));
        analyzer.addTransaction(new Transaction(4, 500, "Store A", "acc4", baseTime + 2000));

        System.out.println("Classic Two-Sum:");
        analyzer.findTwoSum(500);

        System.out.println("\nTwo-Sum with 1-hour window:");
        analyzer.findTwoSumWithTime(500);

        System.out.println("\nDuplicate Detection:");
        analyzer.detectDuplicates();

        System.out.println("\nK-Sum (k=3, target=1000):");
        analyzer.findKSum(3, 1000);
    }
}