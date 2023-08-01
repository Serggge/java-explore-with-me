package ru.practicum.explorewithme.exception.illegal;

public class EventRequestExistException extends EntityExistsException {

    public EventRequestExistException(String message) {
        super(message);
    }
}
