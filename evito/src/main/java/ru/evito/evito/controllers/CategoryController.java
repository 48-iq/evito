package ru.evito.evito.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evito.evito.models.Category;
import ru.evito.evito.repositories.CategoryRepository;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired private CategoryRepository categoryRepository;
    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll().stream().map(Category::getTitle)
                .toList());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<String>> addCategory(@RequestBody List<String> categories) {
        for (var category : categories) {
            if (category == null || category.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
        }
        var allCategories = categoryRepository.findAll();
        for (var category : categories) {
            if (allCategories.stream().noneMatch(c -> c.getTitle().equals(category))) {
                categoryRepository.save(Category.builder().title(category).build());
            }
        }
        for (var category: allCategories) {
            if (categories.stream().noneMatch(c -> c.equals(category.getTitle()))) {
                categoryRepository.deleteCategoryRelationsById(category.getId());
                categoryRepository.delete(category);
            }
        }
        return ResponseEntity.ok(categoryRepository.findAll().stream().map(Category::getTitle)
                .toList());
    }
}
