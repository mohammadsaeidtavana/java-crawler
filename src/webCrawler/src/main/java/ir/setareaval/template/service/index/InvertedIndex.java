package ir.setareaval.template.service.index;

import ir.setareaval.template.domain.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
