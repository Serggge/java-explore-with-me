package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import java.util.List;

public interface CategoryMapper {

    Category mapToCategory(NewCategoryDto dto);

    Category mapToCategory(CategoryDto dto);

    CategoryDto mapToDto(Category category);

    List<CategoryDto> mapToDto(Iterable<Category> categories);
}
