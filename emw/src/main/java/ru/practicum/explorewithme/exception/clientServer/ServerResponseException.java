package ru.practicum.explorewithme.exception.clientServer;

public class ServerResponseException extends RuntimeException {

    public ServerResponseException(String message) {
        super(message);
    }

    public ServerResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
