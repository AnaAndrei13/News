package com.example.News.service;

import com.example.News.model.News;
import com.example.News.model.NewsGroup;
import com.example.News.model.Publisher;
import com.example.News.repository.NewsGroupRepository;
import com.example.News.repository.NewsRepository;
import com.example.News.repository.PublisherRepository;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    NewsGroupRepository newsGroupRepository;

    private static final String FEED_URL = "https://rss.nytimes.com/services/xml/rss/nyt/World.xml";
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



    @Transactional
    public int importFeed() {
        SyndFeed feed;
        try {
            feed = new HttpURLFeedFetcher().retrieveFeed(new URL(FEED_URL));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Feed indisponibil");
        }

        // Editorul se deduce din <copyright>, pentru ca RSS nu are un camp dedicat
        String copyright = feed.getCopyright();
        String publisherName = (copyright == null) ? "" :
                copyright.replaceFirst("(?i)^copyright\\s*(©\\s*)?\\d{4}\\s*", "").trim();
        if (publisherName.isEmpty() && feed.getLink() != null) {
            publisherName = URI.create(feed.getLink()).getHost();
        }

        Publisher publisher = new Publisher();
        publisher.setName(publisherName);
        publisherRepository.save(publisher);

        NewsGroup group = new NewsGroup();
        group.setTitle(feed.getTitle());
        group.setLink(feed.getLink());
        group.setFeedUrl(FEED_URL);
        group.setDescription(feed.getDescription());
        group.setLanguage(feed.getLanguage());
        group.setCopyright(feed.getCopyright());
        group.setPublisher(publisher);
        newsGroupRepository.save(group);

        List<News> list = new ArrayList<>();
        for (SyndEntry entry : feed.getEntries()) {
            News news = new News();
            news.setTitle(entry.getTitle());
            news.setLink(entry.getLink());
            news.setDescription(entry.getDescription() == null ? null : entry.getDescription().getValue());
            news.setPublishedDate(entry.getPublishedDate());
            news.setNewsGroup(group);
            list.add(news);
        }
        newsRepository.saveAll(list);
        return list.size();
    }
}
