package com.study.model;

public class BoardSearchCondition {
    private String startDate;

    private String endDate;

    private String categoryId;

    private String keyword;

    public BoardSearchCondition(String startDate, String endDate, String categoryId, String keyword) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
        this.keyword = keyword;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
