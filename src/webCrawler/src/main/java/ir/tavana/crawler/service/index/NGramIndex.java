package ir.tavana.crawler.service.index;

import ir.tavana.crawler.domain.Document;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@Service
public class NGramIndex {

    private Map<String, Set<Integer>> ngramIndex = new HashMap<>();

    public void addDocument(Document doc) {
        List<String> t = doc.getTokens();
        for (int i = 0; i < t.size() - 1; i++) {
            String bigram = t.get(i) + "_" + t.get(i + 1);
            ngramIndex
                    .computeIfAbsent(bigram, k -> new HashSet<>())
                    .add(doc.getId());
        }
    }
}
