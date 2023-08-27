package ru.practicum.explorewithme.exception.illegal;

public class CategoryExistException extends EntityExistsException {

    public CategoryExistException(String message) {
        super(message);
    }
}
