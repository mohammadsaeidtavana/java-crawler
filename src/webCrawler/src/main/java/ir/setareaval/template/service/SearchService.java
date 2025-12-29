package ir.setareaval.template.service;

import ir.setareaval.template.domain.Document;
import ir.setareaval.template.domain.SearchResult;
import ir.setareaval.template.service.crawler.WebCrawler;
import ir.setareaval.template.service.embedding.Word2VecTrainer;
import ir.setareaval.template.service.index.InvertedIndex;
import ir.setareaval.template.service.preprocess.TextPreprocessor;
import ir.setareaval.template.service.retrieval.CosineSimilarity;
import ir.setareaval.template.service.retrieval.TfIdfCalculator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SearchService {
    private final InvertedIndex index;
    private final TextPreprocessor preprocessor;
    private final TfIdfCalculator tfidf;
    private final CosineSimilarity cosine;
    private final List<Document> documents = new ArrayList<>();
    private final WebCrawler crawler;
    private final Word2VecTrainer word2VecTrainer;

    public SearchService(InvertedIndex index,
                         TextPreprocessor preprocessor,
                         TfIdfCalculator tfidf,
                         CosineSimilarity cosine,
                         Word2VecTrainer word2VecTrainer) {
        this.index = index;
        this.preprocessor = preprocessor;
        this.tfidf = tfidf;
        this.cosine = cosine;
        this.word2VecTrainer = word2VecTrainer;
        this.crawler = new WebCrawler(preprocessor);

        // کرال و ایندکس کردن اسناد
        try {
            List<Document> docs = crawler.crawl("https://en.wikipedia.org/wiki/Category:Technology", 800);
            documents.addAll(docs);
            int id = 1;
            for (Document doc : docs) {
                System.out.println("=={" + id + "}==== document url { " + doc.getUrl() + " }");
                index.addDocument(doc);
                id++;
            }


            // آموزش Word2Vec روی اسناد
            word2VecTrainer.train(documents);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SearchResult> search(String query) {
        // پیش‌پردازش کوئری با n-gram
        List<String> qTokens = preprocessor.preprocess(query, 2);

        // بردار کوئری با TF-IDF
        Map<String, Double> qVector = new HashMap<>();
        for (String term : qTokens) {
            int df = index.getPostings(term).size();
            if (df == 0) continue;
            qVector.put(term, tfidf.tf(1) * tfidf.idf(documents.size(), df));
        }

        List<SearchResult> results = new ArrayList<>();

        for (Document doc : documents) {
            // بردار سند با TF-IDF
            Map<String, Double> dVector = new HashMap<>();
            for (String term : qVector.keySet()) {
                int tf = index.getPostings(term).getOrDefault(doc.getId(), 0);
                if (tf > 0) {
                    dVector.put(term, tfidf.tf(tf) * tfidf.idf(documents.size(), index.getPostings(term).size()));
                }
            }

            // شباهت کسینوسی TF-IDF
            double tfidfScore = cosine.cosine(qVector, dVector);

            // شباهت معنایی Word2Vec
            double semanticScore = 0.0;
            List<String> docTokens = doc.getTokens();
            for (String qToken : qTokens) {
                for (String dToken : docTokens) {
                    semanticScore += word2VecTrainer.similarity(qToken, dToken);
                }
            }
            if (!docTokens.isEmpty()) {
                semanticScore /= docTokens.size();
            }

            // ترکیب امتیاز TF-IDF و Word2Vec
            double score = 0.7 * tfidfScore + 0.3 * semanticScore;

            if (score > 0) {
                results.add(new SearchResult(doc.getId(), doc.getUrl(), score));
            }
        }

        // مرتب‌سازی نتایج
        return results.stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .toList();
    }
}




