package com.trial.gallery.controller;

import com.trial.gallery.dto.ImageDto;
import com.trial.gallery.dto.ImageResponseDto;
import com.trial.gallery.entity.CategoryWithThumbnail;
import com.trial.gallery.entity.Image;
import com.trial.gallery.exceptions.ImageNotFoundException;
import com.trial.gallery.service.ImageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @ApiResponse(responseCode = "200")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("categoryName") String categoryName,
            @RequestParam("imageFile") MultipartFile imageFile) {

        // Log to check if the file and category name are being received
        if (imageFile.isEmpty()) {
            return new ResponseEntity<>("Image file is missing", HttpStatus.BAD_REQUEST);
        }

        try {
            // Debug logging
            System.out.println("Category: " + categoryName);
            System.out.println("File received: " + imageFile.getOriginalFilename());

            ImageDto imageDto = new ImageDto();
            imageDto.setCategoryName(categoryName);
            imageDto.setImageFile(imageFile);

            imageService.saveImageWithCategory(imageDto);

            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<String>> getImagesByCategory(@PathVariable String categoryName) {
        try {
            List<byte[]> images = imageService.downloadImagesByCategory(categoryName);

            // Convert byte arrays to Base64 strings
            List<String> base64Images = images.stream()
                    .map(image -> Base64.getEncoder().encodeToString(image)) // Convert to Base64
                    .collect(Collectors.toList());

            return new ResponseEntity<>(base64Images, HttpStatus.OK); // Return as a list of Base64 strings
        } catch (ImageNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categoryById/{id}")
    public ResponseEntity<Resource> getImageById(@PathVariable Long id) {
        ByteArrayResource byteArrayResource = imageService.trial(id);
        return new ResponseEntity<>(byteArrayResource, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = imageService.getAllCategoryNames(); // Fetch category names only
        return ResponseEntity.ok(categories); // Respond with a list of category names
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryWithThumbnail>> getAllCategoriesWithThumbnails() {
        List<String> categories = imageService.getAllCategoryNames(); // Fetch category names

        // Create a list to hold category with thumbnail objects
        List<CategoryWithThumbnail> categoryWithThumbnails = new ArrayList<>();

        for (String categoryName : categories) {
            try {
                // Fetch the images for this category
                List<byte[]> images = imageService.downloadImagesByCategory(categoryName);

                // Check if there are images in the category
                if (!images.isEmpty()) {
                    // Convert the first image to Base64 for the thumbnail
                    String base64Thumbnail = Base64.getEncoder().encodeToString(images.get(0));

                    // Create a new CategoryWithThumbnail object and add it to the list
                    categoryWithThumbnails.add(new CategoryWithThumbnail(categoryName, base64Thumbnail));
                } else {
                    // In case there are no images, we can add an empty thumbnail or handle it accordingly
                    categoryWithThumbnails.add(new CategoryWithThumbnail(categoryName, null));
                }
            } catch (ImageNotFoundException e) {
                // Handle image not found (optional)
                categoryWithThumbnails.add(new CategoryWithThumbnail(categoryName, null));
            }
        }

        // Return the list of category with thumbnails
        return ResponseEntity.ok(categoryWithThumbnails);
    }

    @GetMapping("/category_id/{categoryName}")
    public ResponseEntity<List<ImageResponseDto>> getImagesByCategoryAndId(@PathVariable String categoryName) {
        try {
            List<ImageResponseDto> images = imageService.downloadImagesByCategoryAndId(categoryName);
            return new ResponseEntity<>(images, HttpStatus.OK); // Return list of ImageResponseDTO
        } catch (ImageNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImageById(@PathVariable Long id) {
        try {
            imageService.deleteById(id);// Call the service method to delete the image
            return new ResponseEntity<>("deleted successfully",HttpStatus.OK); // Return 204 No Content
        } catch (ImageNotFoundException e) {
            return new ResponseEntity<>("Given record id not found",HttpStatus.NOT_FOUND); // Return 404 if image not found
        }
    }

    @DeleteMapping("/deleteCategoryById/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        try {
            imageService.deleteCategory(id);// Call the service method to delete the image
            return new ResponseEntity<>("deleted successfully",HttpStatus.OK); // Return 204 No Content
        } catch (ImageNotFoundException e) {
            return new ResponseEntity<>("Given record id not found",HttpStatus.NOT_FOUND); // Return 404 if image not found
        }

    }
}
