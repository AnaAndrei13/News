package com.example.News.repository;

import com.example.News.model.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface NewsRepository extends CrudRepository<News, Long> {

}
