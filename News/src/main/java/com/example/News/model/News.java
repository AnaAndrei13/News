package com.example.News.model;


import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @Column(unique = true, length = 1000)
    private String link;
    @Column(length = 2000)
    private String description;
    private Date publishedDate;
    @ManyToOne
    @JoinColumn(name = "news_group_id")
    private NewsGroup newsGroup;

    @ManyToMany
    @JoinTable(name = "news_category",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public News(){}

    public News( String title, String link, String description, Date publishedDate) {

        this.title = title;
        this.link = link;
        this.description = description;
        this.publishedDate = publishedDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setNewsGroup(NewsGroup newsGroup) {
        this.newsGroup = newsGroup;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}