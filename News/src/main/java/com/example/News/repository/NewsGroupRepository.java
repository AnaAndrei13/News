package com.example.News.repository;

import com.example.News.model.NewsGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NewsGroupRepository extends CrudRepository<NewsGroup, Long> {
    Optional<NewsGroup> findByFeedUrl(String feedUrl);
}
