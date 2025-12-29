package ir.setareaval.template.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchResult {
    private int docId;
    private String url;
    private double score;
}
