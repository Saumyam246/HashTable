import java.util.*;

public class PlagiarismDetector {

    // n-gram size
    private static final int N = 5;

    // ngram -> set of document IDs
    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    // documentId -> list of ngrams
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();

    // Add document to database
    public void addDocument(String documentId, String text) {

        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(documentId, ngrams);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(documentId);
        }
    }

    // Generate n-grams
    private List<String> generateNgrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }

            grams.add(sb.toString().trim());
        }

        return grams;
    }

    // Analyze new document
    public void analyzeDocument(String documentId, String text) {

        List<String> newDocNgrams = generateNgrams(text);

        System.out.println("Extracted " + newDocNgrams.size() + " n-grams");

        HashMap<String, Integer> matchCounts = new HashMap<>();

        for (String gram : newDocNgrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String docId : ngramIndex.get(gram)) {

                    matchCounts.put(docId,
                            matchCounts.getOrDefault(docId, 0) + 1);
                }
            }
        }

        // Calculate similarity
        for (String docId : matchCounts.keySet()) {

            int matches = matchCounts.get(docId);

            double similarity =
                    (matches * 100.0) / newDocNgrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + docId + "\"");

            System.out.printf("Similarity: %.2f%%", similarity);

            if (similarity > 60) {
                System.out.println(" (PLAGIARISM DETECTED)");
            } else if (similarity > 10) {
                System.out.println(" (suspicious)");
            } else {
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        // Existing essays
        detector.addDocument("essay_089.txt",
                "data structures and algorithms are important concepts in computer science");

        detector.addDocument("essay_092.txt",
                "data structures and algorithms are important concepts in computer science for problem solving");

        // New student essay
        String newEssay =
                "data structures and algorithms are important concepts in computer science for students";

        System.out.println("Analyzing essay_123.txt");
        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}