package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.user.model.User;
import java.util.List;

public interface UserMapper {

    User mapToUser(NewUserRequest dto);

    User mapToUser(UserDto dto);

    UserDto mapToDto(User user);

    List<UserDto> mapToDto(Iterable<User> users);

    UserShortDto mapToShortDto(User user);

    List<UserShortDto> mapToShortDto(Iterable<User> users);
}
