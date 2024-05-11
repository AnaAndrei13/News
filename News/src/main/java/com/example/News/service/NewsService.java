package com.example.News.service;

import com.example.News.model.News;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    public List<News> fetchNewsFromRSS() {
        List<News> newsList = new ArrayList<>();
        try {
            URL feedUrl = new URL("https://rss.nytimes.com/services/xml/rss/nyt/World.xml");
            FeedFetcher feedFetcher = new HttpURLFeedFetcher();
            SyndFeed syndFeed = feedFetcher.retrieveFeed(feedUrl);

            List<SyndEntry> entries = syndFeed.getEntries();
            for (SyndEntry entry : entries) {
                News news = new News();
                news.setTitle(entry.getTitle());
                news.setLink(entry.getLink());
                news.setDescription(entry.getDescription().getValue());
                news.setPublishedDate(entry.getPublishedDate());
                newsList.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }


}
