package ru.practicum.explorewithme.exception.illegal;

public class UserMatchingException extends RuntimeException {

    public UserMatchingException(String message) {
        super(message);
    }
}
