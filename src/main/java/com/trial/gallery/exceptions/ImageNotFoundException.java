package com.trial.gallery.exceptions;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String s) {
        super("Image not found.");
    }
}
