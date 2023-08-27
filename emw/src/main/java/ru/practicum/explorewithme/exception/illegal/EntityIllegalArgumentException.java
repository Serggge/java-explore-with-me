package ru.practicum.explorewithme.exception.illegal;

public abstract class EntityIllegalArgumentException extends IllegalArgumentException {

    public EntityIllegalArgumentException(String message) {
        super(message);
    }
}
