package com.example.News.model;

import jakarta.persistence.*;

@Entity
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Publisher() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
