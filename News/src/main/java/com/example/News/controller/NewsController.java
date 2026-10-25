package com.example.News.controller;

import com.example.News.model.News;
import com.example.News.repository.NewsRepository;
import com.example.News.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class NewsController {
    @Autowired
    NewsService newsService;
@Autowired
    NewsRepository newsRepository;


  //  Return news list sorted by publication date asc/desc or title alphabetically asc/desc.
    @GetMapping(path="/news/all" )
    public List<News> getSortedNews(@RequestParam(value = "sortBy", defaultValue = "date_desc") String sortBy,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size){

        List<News> sortedList = newsService.fetchNewsFromRSS(); //  analyze RSS-ul

        // Sorting logic
        if (sortBy.equalsIgnoreCase("date_asc")) {
            sortedList.sort(Comparator.comparing(News::getPublishedDate));
        } else if (sortBy.equalsIgnoreCase("date_desc")) {
            sortedList.sort(Comparator.comparing(News::getPublishedDate).reversed());
        } else if (sortBy.equalsIgnoreCase("title_asc")) {
            sortedList.sort(Comparator.comparing(News::getTitle));
        } else if (sortBy.equalsIgnoreCase("title_desc")) {
            sortedList.sort(Comparator.comparing(News::getTitle).reversed());
        }
        else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "sortBy invalid. Valori permise: date_asc, date_desc, title_asc, title_desc");
        }
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "page trebuie sa fie >= 0, iar size intre 1 si 100");
        }
        int from = Math.min(page * size, sortedList.size());
        int to = Math.min(from + size, sortedList.size());
        return sortedList.subList(from, to);
    }

//Return the news containing the searched word.
        @GetMapping(path="/news", params="keyword")
        public List<News> searchNews(@RequestParam(value = "keyword") String keyword) {
            List<News> searchList = newsService.fetchNewsFromRSS();
            // Filtering logic based on keyword
        if ( !keyword.isEmpty()) {
            searchList = searchList.stream()
                    .filter(news -> news.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                            news.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return searchList;
    }

//Endpoint for saving news
    @PostMapping("/news")
    public ResponseEntity<String> saveNews() {
        int n = newsService.importFeed();
        return new ResponseEntity<>("Saved " + n + " news", HttpStatus.CREATED);
    }

}
