package ir.tavana.crawler.domain;


import ir.tavana.crawler.service.preprocess.TextPreprocessor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    private int id;
    private String url;
    private String rawText;
    private List<String> tokens;

    public Document(int id, String url, String text, TextPreprocessor preprocessor) {
        this.id = id;
        this.url = url;
        this.rawText = text;
        this.tokens = preprocessor.preprocess(text,2);
    }

    public List<String> getTokens() {
        return tokens;
    }
}
