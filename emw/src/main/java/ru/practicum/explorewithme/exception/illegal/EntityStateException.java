package ru.practicum.explorewithme.exception.illegal;

public abstract class EntityStateException extends IllegalStateException {

    protected EntityStateException(String message) {
        super(message);
    }
}
