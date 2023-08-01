package ru.practicum.explorewithme.category.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.service.CatergoryMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CatergoryMapper {

    @Override
    public Category mapToCategory(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    @Override
    public Category mapToCategory(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    @Override
    public CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    @Override
    public List<CategoryDto> mapToDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(mapToDto(category));
        }
        return result;
    }
}
