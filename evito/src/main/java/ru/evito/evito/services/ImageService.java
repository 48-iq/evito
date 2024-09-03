package ru.evito.evito.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import ru.evito.evito.exceptions.ImageNotFoundException;
import ru.evito.evito.models.Image;
import ru.evito.evito.repositories.ImageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired private ImageRepository imageRepository;

    private String generateFilename() {
        var uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String saveImage(byte[] imageData) {
        var filename = generateFilename();
        try {
            if (!Files.exists(Path.of("/images"))) {
                Files.createDirectory(Path.of("/images"));
            }
            if (!Files.exists(Path.of("/images/" + filename))) {
                Files.createFile(Path.of("/images/" + filename));
            }
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
        try (var outputStream = new FileOutputStream("/images/" + filename)) {
            outputStream.write(imageData);
            Image image = Image.builder()
                    .filename(filename)
                    .filepath("/images/" + filename)
                    .build();
            imageRepository.save(image);
            return image.getFilename();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public void rewriteImage(String filename, byte[] imageData) {
        var image = imageRepository.findImageByFilename(filename)
                .orElseThrow(() -> new ImageNotFoundException("Image with such filename doesn't exist"));
        try (var outputStream = new FileOutputStream(image.getFilepath())) {
            outputStream.write(imageData);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public InputStreamResource getImage(String filename) {
        try {
            var image = imageRepository.findImageByFilename(filename)
                    .orElseThrow(() -> new ImageNotFoundException("Image with such filename doesn't exist"));
            return new InputStreamResource(new FileInputStream(image.getFilepath()));
        } catch (FileNotFoundException exc) {
            throw new RuntimeException(exc);
        }
    }
}
