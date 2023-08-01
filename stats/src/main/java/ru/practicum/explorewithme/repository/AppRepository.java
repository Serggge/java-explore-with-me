package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Application;
import java.util.Optional;

public interface AppRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByUriAndName(String uri, String name);

}
