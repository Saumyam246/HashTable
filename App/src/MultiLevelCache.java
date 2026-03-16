import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String id, String content) {
        this.videoId = id;
        this.content = content;
    }
}

public class MultiLevelCache {

    // L1 Cache (memory)
    private LinkedHashMap<String, VideoData> L1 =
            new LinkedHashMap<String, VideoData>(10000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 10000;
                }
            };

    // L2 Cache (SSD simulation)
    private LinkedHashMap<String, VideoData> L2 =
            new LinkedHashMap<String, VideoData>(100000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 100000;
                }
            };

    // L3 Database simulation
    private HashMap<String, VideoData> database = new HashMap<>();

    // Access count tracking
    private HashMap<String, Integer> accessCount = new HashMap<>();

    // Statistics
    int L1Hits = 0, L2Hits = 0, L3Hits = 0;

    public MultiLevelCache() {

        // Simulated database videos
        for (int i = 1; i <= 1000; i++) {
            database.put("video_" + i,
                    new VideoData("video_" + i, "VideoContent" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 check
        if (L1.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");

            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS (0.5ms)");

        // L2 check
        if (L2.containsKey(videoId)) {

            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);

            promoteToL1(video);

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        VideoData video = database.get(videoId);

        if (video != null) {

            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            L2.put(videoId, video);

            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);

            return video;
        }

        return null;
    }

    // Promote to L1
    private void promoteToL1(VideoData video) {

        L1.put(video.videoId, video);
        accessCount.put(video.videoId,
                accessCount.getOrDefault(video.videoId, 0) + 1);

        System.out.println("Promoted to L1");
    }

    // Cache statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics:");

        System.out.println("L1 Hits: " + L1Hits);
        System.out.println("L2 Hits: " + L2Hits);
        System.out.println("L3 Hits: " + L3Hits);

        double overallHitRate =
                ((L1Hits + L2Hits) * 100.0) / total;

        System.out.printf("Overall Hit Rate: %.2f%%\n", overallHitRate);
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_123");

        System.out.println();

        cache.getVideo("video_123");

        System.out.println();

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}