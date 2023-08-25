package ru.practicum.explorewithme.category.service;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import java.util.List;

public interface CatergoryMapper {

    Category mapToCategory(NewCategoryDto dto);

    Category mapToCategory(CategoryDto dto);

    CategoryDto mapToDto(Category category);

    List<CategoryDto> mapToDto(Iterable<Category> categories);
}
