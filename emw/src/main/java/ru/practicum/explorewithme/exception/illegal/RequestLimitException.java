package ru.practicum.explorewithme.exception.illegal;

public class RequestLimitException extends LimitException {

    public RequestLimitException(String message) {
        super(message);
    }
}
