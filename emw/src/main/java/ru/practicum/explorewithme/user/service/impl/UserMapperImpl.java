package ru.practicum.explorewithme.user.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User mapToUser(NewUserRequest dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return user;
    }

    @Override
    public User mapToUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return user;
    }

    @Override
    public UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @Override
    public List<UserDto> mapToDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(mapToDto(user));
        }
        return result;
    }

    @Override
    public UserShortDto mapToShortDto(User user) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    @Override
    public List<UserShortDto> mapToShortDto(Iterable<User> users) {
        List<UserShortDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(mapToShortDto(user));
        }
        return result;
    }
}
