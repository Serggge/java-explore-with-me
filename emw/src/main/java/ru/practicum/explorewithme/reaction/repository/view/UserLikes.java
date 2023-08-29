package ru.practicum.explorewithme.reaction.repository.view;

import lombok.Getter;
import ru.practicum.explorewithme.user.model.User;

@Getter
public class UserLikes {

    private final User initiator;
    private final Long likes;

    public UserLikes(User initiator, Long likes) {
        this.initiator = initiator;
        this.likes = likes;
    }
}
