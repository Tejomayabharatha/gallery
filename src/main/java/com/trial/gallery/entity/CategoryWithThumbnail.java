package com.trial.gallery.entity;

public class CategoryWithThumbnail {
    private String categoryName;
    private String thumbnailImage;

    public CategoryWithThumbnail(String categoryName, String thumbnailImage) {
        this.categoryName = categoryName;
        this.thumbnailImage = thumbnailImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
}

