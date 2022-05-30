package com.example.proect;

public class Upload {
    private String type;
    private String imageUrl;

    public Upload() {
        //empty constructor 4 firebase
    }

    public Upload(String type, String imageUrl) {

        this.type = type;
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
