package ru.evito.evito.services;

import org.springframework.stereotype.Service;
import ru.evito.evito.dto.product.ProductCreateDto;
import ru.evito.evito.dto.product.ProductUpdateDto;

import java.util.regex.Pattern;

@Service
public class ProductValidationService {

    public boolean isTitleValid(String name) {
        if (name == null || name.isBlank())
            return false;
        var pattern = Pattern.compile("^.{1,200}$");
        return pattern.matcher(name).matches();
    }

    public boolean isDescriptionValid(String description) {
        if (description == null || description.isBlank())
            return true;
        var pattern = Pattern.compile("^.{1,2000}$");
        return pattern.matcher(description).matches();
    }

    public boolean isPriceValid(Double price) {
        if (price == null)
            return false;
        return price >= 0;
    }

    public boolean isProductCreateValid(ProductCreateDto productCreateDto) {

        return isTitleValid(productCreateDto.getTitle()) &&
                isDescriptionValid(productCreateDto.getDescription()) &&
                isPriceValid(productCreateDto.getPrice());
    }

    public boolean isProductUpdateValid(ProductUpdateDto productCreateDto) {
        return isTitleValid(productCreateDto.getTitle()) &&
                isDescriptionValid(productCreateDto.getDescription()) &&
                isPriceValid(productCreateDto.getPrice());
    }


}
