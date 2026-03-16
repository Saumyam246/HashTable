import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheSystem {

    // Max cache size
    private static final int MAX_CACHE_SIZE = 5;

    // LRU Cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            };

    private int cacheHits = 0;
    private int cacheMiss = 0;

    // Resolve domain
    public String resolve(String domain) {

        long startTime = System.nanoTime();

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {

                cacheHits++;

                long endTime = System.nanoTime();
                double time = (endTime - startTime) / 1_000_000.0;

                System.out.println("Cache HIT → " + entry.ipAddress +
                        " (retrieved in " + time + " ms)");

                return entry.ipAddress;
            }

            // expired entry
            cache.remove(domain);
            System.out.println("Cache EXPIRED");
        }

        // Cache miss
        cacheMiss++;

        String ip = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 5); // TTL 5 seconds
        cache.put(domain, newEntry);

        System.out.println("Cache MISS → Query upstream → " + ip + " (TTL: 5s)");

        return ip;
    }

    // Simulated upstream DNS query
    private String queryUpstreamDNS(String domain) {

        Random rand = new Random();

        return "172.217.14." + (rand.nextInt(200) + 1);
    }

    // Cache statistics
    public void getCacheStats() {

        int total = cacheHits + cacheMiss;

        double hitRate = total == 0 ? 0 : (cacheHits * 100.0 / total);

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Miss: " + cacheMiss);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCacheSystem dns = new DNSCacheSystem();

        dns.resolve("google.com");
        dns.resolve("google.com");

        Thread.sleep(6000); // wait for TTL expiration

        dns.resolve("google.com");

        dns.getCacheStats();
    }
}