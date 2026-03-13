import java.util.*;

class AnalyticsSystem {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming page view event
    public void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Count traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }


    // Display dashboard
    public void getDashboard() {

        System.out.println("\nTop Pages:");

        // PriorityQueue to get top pages
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        int rank = 1;

        while (!pq.isEmpty() && rank <= 10) {

            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" + unique + " unique)");

            rank++;
        }


        // Traffic Source Statistics
        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": " +
                    String.format("%.2f", percent) + "%");
        }
    }
}


public class problem5 {

    public static void main(String[] args) {

        AnalyticsSystem system = new AnalyticsSystem();

        // Simulated page view events
        system.processEvent("/article/breaking-news", "user_123", "google");
        system.processEvent("/article/breaking-news", "user_456", "facebook");
        system.processEvent("/sports/championship", "user_789", "google");
        system.processEvent("/sports/championship", "user_123", "direct");
        system.processEvent("/article/breaking-news", "user_999", "google");
        system.processEvent("/tech/ai-future", "user_111", "direct");
        system.processEvent("/tech/ai-future", "user_222", "google");

        // Show dashboard
        system.getDashboard();
    }
}