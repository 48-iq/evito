package ru.evito.evito.dto.product;

import lombok.*;
import ru.evito.evito.models.Category;
import ru.evito.evito.models.Product;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Integer id;
    private String title;
    private String description;
    private String image;
    private Double price;
    private List<String> categories;
    private String username;

    public static ProductDto fromProduct(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .categories(product.getCategories().stream().map(Category::getTitle).toList())
                .username(product.getUser().getUsername())
                .build();
    }
}
