package ru.practicum.explorewithme.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.user.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select user " +
            "from User user " +
            "where user.id in :ids")
    Page<User> findAllById(Iterable<Long> ids, Pageable page);

}
