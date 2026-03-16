import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class AnalyticsDashboard {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process event
    public void processEvent(PageEvent event) {

        // Update page views
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Track traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Get dashboard
    public void getDashboard() {

        System.out.println("\nTop Pages:");

        // Sort pages by views
        List<Map.Entry<String, Integer>> pages =
                new ArrayList<>(pageViews.entrySet());

        pages.sort((a, b) -> b.getValue() - a.getValue());

        int rank = 1;

        for (Map.Entry<String, Integer> entry : pages) {

            if (rank > 10) break;

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" +
                    unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.printf("%s: %.1f%%\n", source, percent);
        }
    }

    public static void main(String[] args) {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        // Simulated page view events
        dashboard.processEvent(new PageEvent("/article/breaking-news", "user_123", "Google"));
        dashboard.processEvent(new PageEvent("/article/breaking-news", "user_456", "Facebook"));
        dashboard.processEvent(new PageEvent("/sports/championship", "user_789", "Direct"));
        dashboard.processEvent(new PageEvent("/article/breaking-news", "user_123", "Google"));
        dashboard.processEvent(new PageEvent("/sports/championship", "user_999", "Google"));
        dashboard.processEvent(new PageEvent("/tech/ai-future", "user_555", "Direct"));

        dashboard.getDashboard();
    }
}