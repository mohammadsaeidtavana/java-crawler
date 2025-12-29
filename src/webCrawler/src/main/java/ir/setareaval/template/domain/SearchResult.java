package ir.setareaval.template.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchResult {
    private int docId;
    private String url;
    private double score;
}
