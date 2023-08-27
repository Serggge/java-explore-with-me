package ru.practicum.explorewithme.exception.illegal;

public abstract class EntityExistsException extends RuntimeException {

    public EntityExistsException(String message) {
        super(message);
    }
}
