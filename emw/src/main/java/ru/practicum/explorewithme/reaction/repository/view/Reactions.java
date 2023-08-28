package ru.practicum.explorewithme.reaction.repository.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reactions {

    private Long id;
    private Long likes;
    private Long dislikes;
}
