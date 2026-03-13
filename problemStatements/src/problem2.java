import java.util.*;

class InventorySystem {

    // productId -> stock count
    private HashMap<String, Integer> stock = new HashMap<>();

    // productId -> waiting list (FIFO)
    private HashMap<String, LinkedHashMap<Integer, Integer>> waitingList = new HashMap<>();


    // Add product with stock
    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedHashMap<>());
    }


    // Check stock
    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }


    // Purchase item (thread safe)
    public synchronized void purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            System.out.println("User " + userId + " → Purchase Success, "
                    + (currentStock - 1) + " units remaining");
        }
        else {
            LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
            int position = queue.size() + 1;
            queue.put(userId, position);

            System.out.println("User " + userId + " → Added to waiting list, position #" + position);
        }
    }
}


public class problem2 {

    public static void main(String[] args) {

        InventorySystem system = new InventorySystem();

        // Add product with limited stock
        system.addProduct("IPHONE15_256GB", 5);

        System.out.println("Stock Available: "
                + system.checkStock("IPHONE15_256GB") + " units\n");

        // Simulate purchases
        system.purchaseItem("IPHONE15_256GB", 101);
        system.purchaseItem("IPHONE15_256GB", 102);
        system.purchaseItem("IPHONE15_256GB", 103);
        system.purchaseItem("IPHONE15_256GB", 104);
        system.purchaseItem("IPHONE15_256GB", 105);

        // Stock finished
        system.purchaseItem("IPHONE15_256GB", 106);
        system.purchaseItem("IPHONE15_256GB", 107);
    }
}