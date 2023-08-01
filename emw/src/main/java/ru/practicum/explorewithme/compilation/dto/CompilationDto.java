package ru.practicum.explorewithme.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompilationDto {

    private Long id;
    private TreeSet<EventShortDto> events;
    private Boolean pinned;
    private String title;

}
