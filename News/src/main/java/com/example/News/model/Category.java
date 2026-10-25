package com.example.News.model;


import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"}))
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    public Category() {}
    public Category(String name, String type) {
        this.name = name;
        this.type = type;
    }

}
