package com.example.frontflix;

public class MovieItem {
    protected String name;
    protected String imgUrl;
    public MovieItem(String name, String email, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }
    public String getName() { return name; }

    public String getImgUrl() { return imgUrl; }
}
