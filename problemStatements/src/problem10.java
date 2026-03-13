import java.util.*;

class VideoData {
    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class MultiLevelCache {

    private int L1_CAPACITY = 10000;
    private int L2_CAPACITY = 100000;

    // L1 Cache (Memory)
    private LinkedHashMap<String, VideoData> L1;

    // L2 Cache (SSD simulated)
    private LinkedHashMap<String, VideoData> L2;

    // L3 Database simulation
    private HashMap<String, VideoData> database;

    // Access counter
    private HashMap<String, Integer> accessCount;

    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;

    public MultiLevelCache() {

        L1 = new LinkedHashMap<String, VideoData>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new LinkedHashMap<String, VideoData>(L2_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_CAPACITY;
            }
        };

        database = new HashMap<>();
        accessCount = new HashMap<>();
    }

    // Add video to database
    public void addVideoToDatabase(String id, String content) {
        database.put(id, new VideoData(id, content));
    }

    // Get video
    public VideoData getVideo(String id) {

        // L1 lookup
        if (L1.containsKey(id)) {
            L1Hits++;
            System.out.println("L1 Cache HIT");
            return L1.get(id);
        }

        System.out.println("L1 Cache MISS");

        // L2 lookup
        if (L2.containsKey(id)) {
            L2Hits++;
            System.out.println("L2 Cache HIT");

            VideoData video = L2.get(id);

            promoteToL1(id, video);

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database lookup
        if (database.containsKey(id)) {
            L3Hits++;
            System.out.println("L3 Database HIT");

            VideoData video = database.get(id);

            L2.put(id, video);

            accessCount.put(id, 1);

            return video;
        }

        System.out.println("Video not found");
        return null;
    }

    // Promote video to L1
    private void promoteToL1(String id, VideoData video) {

        int count = accessCount.getOrDefault(id, 0) + 1;

        accessCount.put(id, count);

        if (count > 2) {
            L1.put(id, video);
            System.out.println("Promoted to L1 Cache");
        }
    }

    // Statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics");

        if (total == 0) total = 1;

        System.out.println("L1 Hit Rate: " + (L1Hits * 100.0 / total) + "%");
        System.out.println("L2 Hit Rate: " + (L2Hits * 100.0 / total) + "%");
        System.out.println("L3 Hit Rate: " + (L3Hits * 100.0 / total) + "%");
    }

    // Cache invalidation
    public void invalidate(String id) {

        L1.remove(id);
        L2.remove(id);
        database.remove(id);

        System.out.println("Cache invalidated for " + id);
    }
}

public class problem10 {

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        // Add videos to database
        cache.addVideoToDatabase("video1", "Movie A");
        cache.addVideoToDatabase("video2", "Movie B");
        cache.addVideoToDatabase("video3", "Movie C");

        cache.getVideo("video1"); // L3
        cache.getVideo("video1"); // L2
        cache.getVideo("video1"); // promote to L1
        cache.getVideo("video1"); // L1 hit

        cache.getVideo("video2"); // L3
        cache.getVideo("video3"); // L3

        cache.getStatistics();

        cache.invalidate("video2");
    }
}
