package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import java.util.List;

public interface CategoryService {

    CategoryDto add(NewCategoryDto dto);

    void delete(long catId);

    CategoryDto update(long catId, NewCategoryDto dto);

    CategoryDto getById(long catId);

    Category getCategoryById(long catId);

    List<CategoryDto> getAll(int from, int size);

    Category getCategoryByName(String name);

}
