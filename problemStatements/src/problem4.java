import java.util.*;

class PlagiarismDetector {

    // n-gram -> set of documents containing it
    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    // document -> list of ngrams
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();

    private int N = 5; // 5-gram


    // Break text into n-grams
    private List<String> extractNgrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }


    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = extractNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }

        System.out.println(docId + " → Stored " + ngrams.size() + " n-grams");
    }


    // Analyze document for plagiarism
    public void analyzeDocument(String docId) {

        List<String> ngrams = documentNgrams.get(docId);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            Set<String> docs = ngramIndex.get(gram);

            if (docs != null) {

                for (String d : docs) {

                    if (!d.equals(docId)) {
                        matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                    }
                }
            }
        }

        System.out.println("\nAnalyzing " + docId);
        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String otherDoc : matchCount.keySet()) {

            int matches = matchCount.get(otherDoc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + otherDoc);

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED\n");
            }
        }
    }
}


public class problem4 {

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 =
                "data structures and algorithms are important concepts in computer science";

        String essay2 =
                "data structures and algorithms are very important subjects in computer science";

        String essay3 =
                "machine learning and artificial intelligence are modern technologies";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay3);

        detector.analyzeDocument("essay_092.txt");
    }
}