package com.example.hidnam.firebase_1;

/**
 * Created by hidnam on 30/9/17.
 */

public class Blog {
    private String title;
    private String description;
    private String image;

    public Blog(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
    public Blog() {

    }
    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
