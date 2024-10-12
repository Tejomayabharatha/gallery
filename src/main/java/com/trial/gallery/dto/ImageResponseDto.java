package com.trial.gallery.dto;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {
    private String id;
    private String base64Image;


    public String getId() {
        return id;
    }

    public String getBase64Image() {
        return base64Image;
    }
}

