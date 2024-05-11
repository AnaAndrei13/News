package com.example.News.controller;

import com.example.News.model.News;
import com.example.News.repository.NewsRepository;
import com.example.News.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NewsController {
    @Autowired
    NewsService newsService;
@Autowired
    NewsRepository newsRepository;


  //  Return news list sorted by publication date asc/desc or title alphabetically asc/desc.
    @GetMapping(path="/news/sorted")
    public List<News> getSortedNews(@RequestParam(value = "sortBy", defaultValue = "date_desc") String sortBy){

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
        return sortedList;
    }

//Return the news containing the searched word.
        @GetMapping(path="/news/search")
        public List<News> searchNews(@RequestParam(value = "keyword") String keyword) {
            List<News> searchList = newsService.fetchNewsFromRSS();
            // Filtering logic based on keyword
        if (keyword != null && !keyword.isEmpty()) {
            searchList = searchList.stream()
                    .filter(news -> news.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                            news.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return searchList;
    }

//Endpoint for saving news
    @PostMapping("/news/save")

    public ResponseEntity<String> saveNews(@RequestBody News news) {
        try {
            newsRepository.save(news);
            return new ResponseEntity<>("News saved successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save news: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
