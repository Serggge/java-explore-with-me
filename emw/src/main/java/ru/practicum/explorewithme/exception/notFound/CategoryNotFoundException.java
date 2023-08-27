package ru.practicum.explorewithme.exception.notFound;

public class CategoryNotFoundException extends EntityNotFoundException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
