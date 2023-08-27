package ru.practicum.explorewithme.exception.illegal;

public abstract class LimitException extends EntityIllegalArgumentException {

    public LimitException(String message) {
        super(message);
    }
}
