package ir.setareaval.template.service.crawler;

import ir.setareaval.template.domain.Document;
import ir.setareaval.template.service.preprocess.TextPreprocessor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@Service
public class WebCrawler {

    private final TextPreprocessor preprocessor;

    public WebCrawler(TextPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    public List<Document> crawl(String seed, int limit) throws IOException {

        System.out.println("===========");
        List<Document> docs = new ArrayList<>();
        Queue<String> urls = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        urls.add(seed);
        int counter = 0;
        while (!urls.isEmpty() && docs.size() < limit) {
            String url = urls.poll();
            if (visited.contains(url)) continue;
            visited.add(url);

            try {
                org.jsoup.nodes.Document page = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36")
                        .timeout(10000)
                        .get();

                String text = page.body().text();
                Document doc = new Document(counter++,url, text, preprocessor);
                docs.add(doc);

                // اضافه کردن لینک‌های معتبر و داخل همان دامنه
                page.select("a[href]").forEach(l -> {
                    String link = l.attr("href").trim();       // ابتدا href خام
                    if (link.isEmpty() || link.equals("#") || link.equals("undefined")) return;  // نامعتبر
                    // تبدیل به URL کامل
                    link = l.absUrl("href").trim();
                    if (!link.startsWith(seed)) return;  // فقط دامنه موردنظر
                    if (visited.contains(link)) return;                    // قبلاً بازدید نشده باشد
                    urls.add(link);
                });
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to fetch: " + url);
                // ادامه کرال روی لینک‌های دیگر
            }
        }

        return docs;
    }

//    public List<ir.setareaval.template.domain.Document> crawl(String seed, int limit) throws IOException {
//
//        System.out.println("===========");
//        List<Document> docs = new ArrayList<>();
//        Queue<String> urls = new LinkedList<>();
//        Set<String> visited = new HashSet<>();
//        urls.add(seed);
//
//        while (!urls.isEmpty() && docs.size() < limit) {
//            String url = urls.poll();
//            if (visited.contains(url)) continue;
//            visited.add(url);
//
//            //org.jsoup.nodes.Document page = Jsoup.connect(url).get();
//            org.jsoup.nodes.Document page = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//
//            String text = page.body().text();
//            docs.add(new ir.setareaval.template.domain.Document(url, text));
//
//            page.select("a[href]").forEach(
//                    l -> urls.add(l.absUrl("href"))
//            );
//
//
//        }
//        return docs;
//    }
}
