package com.trial.gallery.service;

import com.trial.gallery.dto.ImageDto;
import com.trial.gallery.dto.ImageResponseDto;
import com.trial.gallery.entity.Category;
import com.trial.gallery.entity.Image;
import com.trial.gallery.entity.ImageData;
import com.trial.gallery.exceptions.ImageNotFoundException;
import com.trial.gallery.repositories.CategoryRepository;
import com.trial.gallery.repositories.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImageServiceImpl.class);

    @Transactional
    @Override
    public void saveImageWithCategory(ImageDto imageDTO) throws IOException {
        String categoryName = imageDTO.getCategoryName();
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        Category category;

        if (categoryOptional.isPresent()) {
            category = categoryOptional.get();
        } else {
            category = new Category();
            category.setName(categoryName);
            // Save the new category to the database
            category = categoryRepository.save(category);
        }

        MultipartFile imageFile = imageDTO.getImageFile();
        byte[] imageData = imageFile.getBytes();

        Image image = new Image();
        image.setPhoto(imageData);
        image.setCategory(category);
        // Save the image
        imageRepository.save(image);

        log.info("Image successfully saved with category " + categoryName);
    }


    @Override
    public List<byte[]> downloadImagesByCategory(String categoryName) throws ImageNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);

        if (categoryOptional.isEmpty()) {
            throw new ImageNotFoundException("Category not found: " + categoryName);
        }

        Long categoryId = categoryOptional.get().getId();
        List<Image> images = imageRepository.findByCategoryId(categoryId);

        if (images.isEmpty()) {
            throw new ImageNotFoundException("No images found for category name: " + categoryName);
        }

        log.info("Category found: {} with {} images", categoryOptional.get().getName(), images.size());
        // Convert image data to ByteArrayResource and return as a list
        return images.stream()
                .map(Image::getPhoto)
                .collect(Collectors.toList());
    }

    public ByteArrayResource trial(Long id) {
        Optional<Image> optional = imageRepository.findById(id);
        Image image = optional.orElseThrow(
                () -> new ImageNotFoundException("IMAGE NOT FOUND"));
        return new ByteArrayResource(image.getPhoto());
    }

    @Override
    public List<String> getAllCategoryNames() {
        List<Category> categories = categoryRepository.findAll(); // Retrieve all categories
        return categories.stream()
                .map(Category::getName)  // Map each Category object to its name
                .collect(Collectors.toList()); // Collect and return the list of names
    }

    @Override
    public Image updateImageById(Long id) throws ImageNotFoundException {
        // Check if the image exists
        if (!imageRepository.existsById(id)) {
            throw new ImageNotFoundException("Image not found");
        }
        Optional<Image> imageId = imageRepository.findById(id);
        // Delete the image
        imageId.get().setPhoto(null);
        return imageRepository.save(imageId.get());

    }

    @Override
    public void deleteById(Long id) throws ImageNotFoundException{
//        if (!imageRepository.existsById(id)) {
//            throw new ImageNotFoundException("Image not found");
//        }
        imageRepository.deleteById(id);
    }

    @Override
    public List<ImageData> getAllImages() {
        return List.of();
    }

    @Override
    public List<ImageResponseDto> downloadImagesByCategoryAndId(String categoryName) throws ImageNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);

        if (categoryOptional.isEmpty()) {
            throw new ImageNotFoundException("Category not found: " + categoryName);
        }

        Long categoryId = categoryOptional.get().getId();
        List<Image> images = imageRepository.findByCategoryId(categoryId);

        if (images.isEmpty()) {
            throw new ImageNotFoundException("No images found for category name: " + categoryName);
        }

        log.info("Category found: {} with {} images", categoryOptional.get().getName(), images.size());

        // Convert each image to a DTO that includes both the image ID and Base64 string
        return images.stream()
                .map(image -> new ImageResponseDto(
                        image.getId().toString(),
                        Base64.getEncoder().encodeToString(image.getPhoto())))
                .collect(Collectors.toList());
    }





}
