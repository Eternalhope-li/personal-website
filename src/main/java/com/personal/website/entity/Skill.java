package com.personal.website.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String category;
    private int sortOrder;
    private String icon;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int s) { this.sortOrder = s; }
    public String getIcon() { return icon; }
    public void setIcon(String i) { this.icon = i; }
}