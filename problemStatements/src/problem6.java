import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    int refillRate; // tokens added per hour
    long lastRefillTime;

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on time
    private void refill() {

        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        long tokensToAdd = (elapsed / 3600000) * refillRate;

        if (tokensToAdd > 0) {
            tokens = (int)Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    // Try to consume token
    public boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        return tokens;
    }
}


class RateLimiter {

    // clientId -> TokenBucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int limit = 1000;

    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(limit, limit));

        TokenBucket bucket = clients.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {
            System.out.println("Allowed (" +
                    bucket.getRemainingTokens() +
                    " requests remaining)");
        }
        else {
            System.out.println("Denied (0 requests remaining)");
        }

        return allowed;
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = limit - bucket.getRemainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + limit +
                ", remaining: " + bucket.getRemainingTokens() + "}");
    }
}


public class problem6 {

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        // Simulate API requests
        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }
}