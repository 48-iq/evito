package ru.evito.evito.dto.product;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDto {
    private String title;
    private String description;
    private Double price;
    private MultipartFile image;
    private List<String> categories;
}
