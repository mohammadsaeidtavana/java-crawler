package ir.tavana.crawler.service.preprocess;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@Service
public class TextPreprocessor {

    public List<String> preprocess(String text, int n) {
        List<String> tokens = new ArrayList<>();

        // جدا کردن تک‌کلمات
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            if (!word.isEmpty()) tokens.add(word);
        }

        // تولید n-gram ها
        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j > 0) sb.append(" ");
                sb.append(words[i + j]);
            }
            tokens.add(sb.toString());
        }

        return tokens;
    }

}
