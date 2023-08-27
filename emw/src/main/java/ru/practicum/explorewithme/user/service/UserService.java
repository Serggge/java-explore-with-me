package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;
import java.util.List;

public interface UserService {

    UserDto add(NewUserRequest userDto);

    void delete(long userId);

    List<UserDto> getAll(Long[] ids, int from, int size);

    User getUserById(long userId);
}
