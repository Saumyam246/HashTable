import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    // query -> frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();

    // Insert query into Trie
    public void addQuery(String query, int freq) {

        frequencyMap.put(query, freq);

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.add(query);
        }
    }

    // Update search frequency
    public void updateFrequency(String query) {

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
    }

    // Get top suggestions
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        List<String> candidates = node.queries;

        PriorityQueue<String> heap =
                new PriorityQueue<>(
                        (a, b) -> frequencyMap.get(b) - frequencyMap.get(a)
                );

        heap.addAll(candidates);

        List<String> results = new ArrayList<>();

        int k = 10;

        while (!heap.isEmpty() && k-- > 0) {
            results.add(heap.poll() +
                    " (" + frequencyMap.get(heap.peek()) + ")");
        }

        return results;
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial", 1234567);
        system.addQuery("javascript", 987654);
        system.addQuery("java download", 456789);
        system.addQuery("java 21 features", 1);

        System.out.println("Search results for 'jav':");

        List<String> suggestions = system.search("jav");

        int rank = 1;
        for (String s : suggestions) {
            System.out.println(rank + ". " + s);
            rank++;
        }

        // Update frequency
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("\nUpdated frequency for 'java 21 features': "
                + system.frequencyMap.get("java 21 features"));
    }
}