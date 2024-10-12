package com.trial.gallery.service;

import com.trial.gallery.dto.ImageDto;
import com.trial.gallery.dto.ImageResponseDto;
import com.trial.gallery.entity.Category;
import com.trial.gallery.entity.CategoryWithThumbnail;
import com.trial.gallery.entity.Image;
import com.trial.gallery.entity.ImageData;
import com.trial.gallery.exceptions.ImageNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    /**
     * Save an image with a given category.
     *
     * @param imageDTO The data transfer object containing image file and category information.
     * @throws IOException If there is an error processing the file.
     */
    void saveImageWithCategory(@NonNull ImageDto imageDTO) throws IOException;

    List<byte[]> downloadImagesByCategory(String categoryName) throws ImageNotFoundException;

     ByteArrayResource trial(Long id);

     List<String>
    getAllCategoryNames();

    Image updateImageById(Long id) throws ImageNotFoundException;

    List<ImageData> getAllImages();

    void deleteById(Long id) throws ImageNotFoundException;


    List<ImageResponseDto> downloadImagesByCategoryAndId(String categoryName);


}


