package ir.setareaval.template.service.embedding;

import ir.setareaval.template.domain.Document;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@Service
public class Word2VecTrainer {
    private Word2Vec vec;

    public void train(List<Document> documents) {
        List<String> sentences = documents.stream()
                .map(Document::getRawText)
                .collect(Collectors.toList());

        SentenceIterator iter = new CollectionSentenceIterator(sentences);
        TokenizerFactory tokenizer = new DefaultTokenizerFactory();
        tokenizer.setTokenPreProcessor(new CommonPreprocessor());

        vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(5)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(tokenizer)
                .build();

        vec.fit();
    }

    public double similarity(String word1, String word2) {
        if (vec.hasWord(word1) && vec.hasWord(word2)) {
            return vec.similarity(word1, word2);
        }
        return 0.0;
    }

    public Word2Vec getModel() {
        return vec;
    }
}
