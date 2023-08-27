package ru.practicum.explorewithme.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.category.service.CategoryMapper;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.illegal.CategoryNotEmptyException;
import ru.practicum.explorewithme.exception.notFound.CategoryNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@Slf4j
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto add(NewCategoryDto dto) {
        Category newCategory = categoryMapper.mapToCategory(dto);
        newCategory = categoryRepository.save(newCategory);
        log.info("Category created: {}", newCategory);
        return categoryMapper.mapToDto(newCategory);
    }

    @Override
    public void delete(long catId) {
        Category category = getCategoryById(catId);
        if (eventRepository.getCountEventsByCategory(catId) > 0) {
            throw new CategoryNotEmptyException(String.format("The category with ID=%d is not empty", catId));
        }
        categoryRepository.delete(category);
        log.info("Category: {} successful deleted", category);
    }

    @Override
    public CategoryDto update(long catId, NewCategoryDto dto) {
        Category categoryById = getCategoryById(catId);
        categoryById.setName(dto.getName());
        categoryById = categoryRepository.save(categoryById);
        log.info("Category with ID={} updated: {}", catId, categoryById);
        return categoryMapper.mapToDto(categoryById);
    }

    @Override
    public CategoryDto getById(long catId) {
        log.debug("Getting category with ID={}", catId);
        Category foundedCat = getCategoryById(catId);
        return categoryMapper.mapToDto(foundedCat);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        log.debug("Getting categories from={}, size={}", from, size);
        Pageable page = PageRequest.of(from, size);
        Page<Category> foundedCats = categoryRepository.findAll(page);
        return categoryMapper.mapToDto(foundedCats);
    }

    @Override
    public Category getCategoryById(long catId) {
        Optional<Category> foundedCat = categoryRepository.findById(catId);
        if (foundedCat.isPresent()) {
            return foundedCat.get();
        } else {
            throw new CategoryNotFoundException(String.format("Category with id=%d was not found", catId));
        }
    }

    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> catFoundedByName = categoryRepository.findByName(name);
        if (catFoundedByName.isPresent()) {
            return catFoundedByName.get();
        } else {
            throw new CategoryNotFoundException(String.format("Category with name=%s not found", name));
        }
    }
}
