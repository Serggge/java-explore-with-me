package ru.practicum.explorewithme.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountByApp {

    private String uri;
    private String app;
    private Long count;

    public CountByApp(String uri, String app, Long count) {
        this.uri = uri;
        this.app = app;
        this.count = count;
    }
}
