package ru.practicum.explorewithme.reaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Popularity {

    private Long eventId;
    private Long rating;

}
