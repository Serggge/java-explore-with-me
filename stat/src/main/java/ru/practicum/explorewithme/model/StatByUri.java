package ru.practicum.explorewithme.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatByUri {

    private String uri;
    private String app;
    private Long count;

    public StatByUri(String uri, String app, Long count) {
        this.uri = uri;
        this.app = app;
        this.count = count;
    }
}
