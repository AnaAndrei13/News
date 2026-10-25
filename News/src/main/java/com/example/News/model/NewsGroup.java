package com.example.News.model;

import jakarta.persistence.*;

@Entity
public class NewsGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String link;
    @Column(unique = true, length = 500)
    private String feedUrl;
    @Column(length = 1000)
    private String description;
    private String language;
    private String copyright;
    @ManyToOne
    private Publisher publisher;

    public NewsGroup() {}
    // getteri și setteri

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
