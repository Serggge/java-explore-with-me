package ru.practicum.explorewithme.exception.illegal;

public class UserAlreadyRegisteredException extends EntityExistsException {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
