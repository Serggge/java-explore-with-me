package ru.practicum.explorewithme.event.model;

import lombok.Getter;

@Getter
public enum TypesForSearching {

    CATEGORY("category"),
    EVENT_TITLE("title"),
    EVENT_DESCRIPTION("description"),
    DATE("date");

    final String name;

    TypesForSearching(String name) {
        this.name = name;
    }

}
