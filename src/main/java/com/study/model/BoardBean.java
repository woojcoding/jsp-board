package com.study.model;

public class BoardBean {
    private Long boardId;

    private Long categoryId;

    private String writer;

    private String password;

    private String title;

    private String content;

    private String views;

    private String createdAt;

    private String modifiedAt;

    public Long getBoardId() {
        return boardId;
    }


    public Long getCategoryId() {
        return categoryId;
    }


    public String getWriter() {
        return writer;
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getViews() {
        return views;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
