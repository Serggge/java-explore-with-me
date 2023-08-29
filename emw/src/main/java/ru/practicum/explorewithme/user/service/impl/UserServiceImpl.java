package ru.practicum.explorewithme.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.notFound.UserNotFoundException;
import ru.practicum.explorewithme.reaction.repository.ReactionRepository;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;
import ru.practicum.explorewithme.user.service.UserMapper;
import ru.practicum.explorewithme.user.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto add(NewUserRequest userDto) {
        User newUser = userMapper.mapToUser(userDto);
        newUser = userRepository.save(newUser);
        log.info("User created: {}", newUser);
        return userMapper.mapToDto(newUser);
    }

    @Override
    public void delete(long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
        log.info("User deleted: {}", user);
    }

    @Override
    public List<UserDto> getAll(Long[] ids, int from, int size) {
        log.debug("Find users by Ids: {}, from={}, size={}", ids, from, size);
        Pageable page = PageRequest.of(from, size);
        Page<User> foundedUsers;
        if (ids != null) {
            foundedUsers = userRepository.findAllById(Arrays.asList(ids), page);
        } else {
            foundedUsers = userRepository.findAll(page);
        }
        return userMapper.mapToDto(foundedUsers);
    }

    @Override
    public User getUserById(long userId) {
        log.debug("Get user with ID={}", userId);
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException(String.format("User with ID=%d not found", userId));
        }
    }

    @Override
    public List<UserShortDto> getPopularInitiators(int from, int size) {
        List<User> users = new ArrayList<>();
        reactionRepository.findPopularInitiators(PageRequest.of(from, size)).forEach(view ->
                users.add(view.getInitiator()));
        return userMapper.mapToShortDto(users);
    }
}
