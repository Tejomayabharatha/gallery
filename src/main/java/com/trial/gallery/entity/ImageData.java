package com.trial.gallery.entity;

public class ImageData {
    private String id;      // Image ID as a String
    private byte[] photo;   // Image data in byte array format

    public ImageData(String id, byte[] photo) {
        this.id = id;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public byte[] getPhoto() {
        return photo;
    }
}

