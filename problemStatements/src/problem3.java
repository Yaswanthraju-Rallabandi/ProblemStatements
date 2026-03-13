import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private int capacity;

    // LRU cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    // Resolve domain
    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            System.out.println("Cache HIT → " + entry.ipAddress);
            return entry.ipAddress;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
            System.out.println("Cache EXPIRED");
        }

        misses++;

        // Simulate upstream DNS lookup
        String ip = queryUpstreamDNS(domain);

        // store with TTL 5 seconds (example)
        cache.put(domain, new DNSEntry(domain, ip, 5));

        System.out.println("Cache MISS → Query upstream → " + ip);

        return ip;
    }

    // Simulated upstream DNS
    private String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + (100 + r.nextInt(100));
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = (total == 0) ? 0 : (hits * 100.0 / total);

        System.out.println("\nCache Statistics:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

public class problem3 {

    public static void main(String[] args) throws Exception {

        DNSCache dns = new DNSCache(5);

        dns.resolve("google.com");   // MISS
        dns.resolve("google.com");   // HIT

        Thread.sleep(6000); // wait for TTL expiration

        dns.resolve("google.com");   // EXPIRED → MISS

        dns.resolve("youtube.com");
        dns.resolve("github.com");
        dns.resolve("openai.com");

        dns.getCacheStats();
    }
}
