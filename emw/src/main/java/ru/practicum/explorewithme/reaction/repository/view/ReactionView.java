package ru.practicum.explorewithme.reaction.repository.view;

import java.util.Optional;

public interface ReactionView {

    Optional<Long> getEventId();

    Optional<Long> getLikes();

    Optional<Long> getDislikes();
}
