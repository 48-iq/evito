package ru.evito.evito.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evito.evito.dto.product.ProductCreateDto;
import ru.evito.evito.dto.product.ProductDto;
import ru.evito.evito.dto.product.ProductPageDto;
import ru.evito.evito.dto.product.ProductUpdateDto;
import ru.evito.evito.services.ProductService;
import ru.evito.evito.services.ProductValidationService;

import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired ProductService productService;
    @Autowired ProductValidationService productValidationService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@ModelAttribute ProductCreateDto productCreateDto) {
        if (!productValidationService.isProductCreateValid(productCreateDto)) {
            return ResponseEntity.badRequest().build();
        }
        var product = productService.createProduct(productCreateDto);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id,
                                                    @ModelAttribute ProductUpdateDto productUpdateDto) {
        if (!productValidationService.isProductUpdateValid(productUpdateDto)) {
            return ResponseEntity.badRequest().build();
        }
        var product = productService.updateProduct(id, productUpdateDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }



    @GetMapping
    public ResponseEntity<ProductPageDto> getAllProducts(@RequestParam Integer page,
                                                         @RequestParam Integer size,
                                                         @RequestParam(required = false) List<String> categories,
                                                         @RequestParam(required = false) String username,
                                                         @RequestParam(required = false) String title) {

        if (username != null && ((title != null && !title.isEmpty()) || (categories!=null && categories.size() > 0)))
            return ResponseEntity.badRequest().build();
        System.out.println(categories);
        System.out.println(title);


        if ((title != null && !title.isEmpty()) && (categories!=null && categories.size() > 0)) {
            return ResponseEntity.ok(productService.getAllProductsByCategoryAndTitle(title, categories, page, size));
        }
        if (categories != null && categories.size() > 0) {

            return ResponseEntity.ok(productService.getAllProductsByCategory(categories, page, size));
        }
        if (username != null) {
            return ResponseEntity.ok(productService.getAllProductsByUser(username, page, size));
        }
        if ((title != null && !title.isEmpty())) {
            return ResponseEntity.ok(productService.getAllProductsByTitle(title, page, size));
        }
        else {
            return ResponseEntity.ok(productService.getAllProducts(page, size));
        }
    }





}
