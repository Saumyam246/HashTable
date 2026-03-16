import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    double refillRate; // tokens per second
    long lastRefillTime;

    TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens
    private void refill() {

        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        int refillTokens = (int) (seconds * refillRate);

        if (refillTokens > 0) {
            tokens = Math.min(maxTokens, tokens + refillTokens);
            lastRefillTime = now;
        }
    }

    // Consume token
    synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    int getRemainingTokens() {
        return tokens;
    }
}

public class RateLimiterSystem {

    // clientId -> TokenBucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private static final int LIMIT = 1000;
    private static final double REFILL_RATE = LIMIT / 3600.0; // per second

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(LIMIT, REFILL_RATE));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" +
                    bucket.getRemainingTokens() +
                    " requests remaining)";
        }

        return "Denied (0 requests remaining, retry later)";
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = LIMIT - bucket.getRemainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + LIMIT +
                ", remaining: " + bucket.getRemainingTokens() + "}");
    }

    public static void main(String[] args) {

        RateLimiterSystem limiter = new RateLimiterSystem();

        // Simulate API requests
        for (int i = 0; i < 5; i++) {
            System.out.println(
                    limiter.checkRateLimit("abc123"));
        }

        limiter.getRateLimitStatus("abc123");
    }
}