package com.example.hidnam.firebase_1;

/**
 * Created by hidnam on 26/9/17.
 */

public class Users {
    public String name;
    public String image;
    public String thumbnail;

    public Users(String name, String image, String thumbnail) {
        this.name = name;
        this.image = image;
        this.thumbnail = thumbnail;
    }
    public Users() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
