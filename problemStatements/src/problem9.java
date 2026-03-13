import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    int time; // minutes for simplicity

    public Transaction(int id, int amount, String merchant, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
    }
}

class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();


    public void addTransaction(Transaction t) {
        transactions.add(t);
    }


    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        System.out.println("\nTwo-Sum Results:");

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction t2 = map.get(complement);

                System.out.println("(" + t2.id + ", " + t.id + ") → "
                        + t2.amount + " + " + t.amount);
            }

            map.put(t.amount, t);
        }
    }


    // Two-Sum within time window (1 hour = 60 min)
    public void twoSumWithTimeWindow(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        System.out.println("\nTwo-Sum within 1 hour:");

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction t2 = map.get(complement);

                if (Math.abs(t.time - t2.time) <= 60) {

                    System.out.println("(" + t2.id + ", " + t.id + ") → "
                            + t2.amount + " + " + t.amount);
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

        System.out.println("\nDuplicate Transactions:");

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate: " + key + " → IDs: ");

                for (Transaction t : list) {
                    System.out.print(t.id + " ");
                }

                System.out.println();
            }
        }
    }


    // Simple K-Sum (k=3 example)
    public void findThreeSum(int target) {

        System.out.println("\n3-Sum Results:");

        int n = transactions.size();

        for (int i = 0; i < n; i++) {

            HashMap<Integer, Transaction> map = new HashMap<>();

            int newTarget = target - transactions.get(i).amount;

            for (int j = i + 1; j < n; j++) {

                int complement = newTarget - transactions.get(j).amount;

                if (map.containsKey(complement)) {

                    Transaction t1 = transactions.get(i);
                    Transaction t2 = map.get(complement);
                    Transaction t3 = transactions.get(j);

                    System.out.println("(" + t1.id + ", " + t2.id + ", " + t3.id + ")");
                }

                map.put(transactions.get(j).amount, transactions.get(j));
            }
        }
    }
}


public class problem9 {

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", 600));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", 615));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", 630));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", 700));

        analyzer.findTwoSum(500);

        analyzer.twoSumWithTimeWindow(500);

        analyzer.detectDuplicates();

        analyzer.findThreeSum(1000);
    }
}