package ru.evito.evito.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.evito.evito.dto.product.ProductCreateDto;
import ru.evito.evito.dto.product.ProductDto;
import ru.evito.evito.dto.product.ProductPageDto;
import ru.evito.evito.dto.product.ProductUpdateDto;
import ru.evito.evito.exceptions.ProductNotFoundException;
import ru.evito.evito.exceptions.UserNotFoundException;
import ru.evito.evito.models.Product;
import ru.evito.evito.repositories.CategoryRepository;
import ru.evito.evito.repositories.ProductRepository;
import ru.evito.evito.repositories.UserRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ImageService imageService;
    @Autowired UserRepository userRepository;

    @Transactional
    public ProductDto createProduct(ProductCreateDto productDto) {
        var product = Product.builder()
                .title(productDto.getTitle())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .user(userRepository.findUserByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()
                ).orElseThrow(() -> new UserNotFoundException("User not found")))
                .categories(
                        categoryRepository.findAllCategoriesByTitles(productDto.getCategories())
                )
                .build();

        if (productDto.getImage() != null) {
            try {
                product.setImage(imageService.saveImage(productDto.getImage().getBytes()));
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        var savedProduct = productRepository.save(product);
        return ProductDto.fromProduct(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Integer id, ProductUpdateDto productDto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setUser(userRepository.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"))
        );
        product.setCategories(
                categoryRepository.findAllCategoriesByTitles(productDto.getCategories())
        );

        if (productDto.getImage() != null) {
            try {
                product.setImage(imageService.saveImage(productDto.getImage().getBytes()));
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        var savedProduct = productRepository.save(product);
        return ProductDto.fromProduct(savedProduct);
    }


    public ProductDto getProductById(Integer id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return ProductDto.fromProduct(product);
    }
    @Transactional
    public ProductPageDto getAllProducts(Integer page, Integer size) {
        var products = productRepository.findAll(PageRequest.of(page, size));
        return ProductPageDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .total(products.getTotalPages())
                .products(products.stream().map(ProductDto::fromProduct).toList())
                .build();

    }
    @Transactional
    public ProductPageDto getAllProductsByCategory(List<String> category, Integer page, Integer size) {
        var products = productRepository.findAllProductsByCategory(category, category.size(),  PageRequest.of(page, size));
        return ProductPageDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .total(products.getTotalPages())
                .products(products.stream().map(ProductDto::fromProduct).toList())
                .build();
    }
    @Transactional
    public ProductPageDto getAllProductsByTitle(String title, Integer page, Integer size) {
        var products = productRepository.findAllProductsByTitle(title, PageRequest.of(page, size));
        return ProductPageDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .total(products.getTotalPages())
                .products(products.stream().map(ProductDto::fromProduct).toList())
                .build();
    }

    @Transactional
    public void deleteProduct(Integer id) {
        var productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            productRepository.deleteProductRelationsById(id);
        }
        productRepository.deleteById(id);
    }
    @Transactional
    public ProductPageDto getAllProductsByUser(String username, Integer page, Integer size) {
        var products = productRepository.findAllProductsByUser(username, PageRequest.of(page, size));
        return ProductPageDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .total(products.getTotalPages())
                .products(products.stream().map(ProductDto::fromProduct).toList())
                .build();
    }

    @Transactional
    public ProductPageDto getAllProductsByCategoryAndTitle(String title, List<String> categories,
                                                           Integer page, Integer size) {

        var products = productRepository.findAllProductsByCategoryAndTitle(title, categories, categories.size(),
                PageRequest.of(page, size));
        return ProductPageDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .total(products.getTotalPages())
                .products(products.stream().map(ProductDto::fromProduct).toList())
                .build();

    }
}
