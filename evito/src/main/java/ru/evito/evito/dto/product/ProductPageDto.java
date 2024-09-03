package ru.evito.evito.dto.product;

import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPageDto {
    private List<ProductDto> products;
    private Integer total;
    private Integer page;
    private Integer size;
}
