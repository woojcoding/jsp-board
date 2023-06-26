package com.study.model;

public class CategoryBean {
    private Long categoryId;

    private String name;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryBean(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
