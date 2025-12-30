package ir.tavana.crawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mohammad saeid tavana
 * @version 1.0
 * @since 2025
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchResult {
    private int docId;
    private String url;
    private double score;
    private String text;

}
