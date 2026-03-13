import java.util.*;

class AutocompleteSystem {

    // query -> frequency
    private HashMap<String, Integer> queryFrequency = new HashMap<>();


    // Add new query or update frequency
    public void updateFrequency(String query) {

        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);

        System.out.println(query + " → Frequency: " + queryFrequency.get(query));
    }


    // Search suggestions
    public void search(String prefix) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            if (entry.getKey().startsWith(prefix)) {
                pq.add(entry);
            }
        }

        System.out.println("\nSuggestions for \"" + prefix + "\":");

        int count = 1;

        while (!pq.isEmpty() && count <= 10) {

            Map.Entry<String, Integer> entry = pq.poll();

            System.out.println(count + ". " + entry.getKey() +
                    " (" + entry.getValue() + " searches)");

            count++;
        }

        if (count == 1) {
            System.out.println("No suggestions found");
        }
    }
}


public class problem7 {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Initial search data
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java tutorial");

        // Search suggestions
        system.search("jav");

        // Update frequency (new search trend)
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}