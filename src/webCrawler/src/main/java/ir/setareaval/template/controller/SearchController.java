package ir.setareaval.template.controller;

import ir.setareaval.template.domain.SearchResult;
import ir.setareaval.template.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
/**
 * @author  mohammad saeid tavana
 * @since 2025
 * @version 1.0
 */
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String q) throws IOException {
        return searchService.search(q);
    }
}
