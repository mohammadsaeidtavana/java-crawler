package ir.tavana.crawler.service.index;

import ir.tavana.crawler.domain.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@Service
public class InvertedIndex {
    private Map<String, Map<Integer, Integer>> index = new HashMap<>();

    public void addDocument(Document doc) {
        for (String term : doc.getTokens()) {
            index
                    .computeIfAbsent(term, k -> new HashMap<>())
                    .merge(doc.getId(), 1, Integer::sum);
        }
    }

    public Map<Integer, Integer> getPostings(String term) {
        return index.getOrDefault(term, Map.of());
    }
}
