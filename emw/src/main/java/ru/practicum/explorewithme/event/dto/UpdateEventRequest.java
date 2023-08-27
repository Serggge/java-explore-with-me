package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.event.model.StateAction;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import static ru.practicum.explorewithme.util.Constants.DATE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateEventRequest implements Categorized {

    @Size(min = 20, max = 2000)
    private String annotation;
    @PositiveOrZero
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = DATE_PATTERN)
    @Future
    private LocalDateTime eventDate;
    @Valid
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @Valid
    private StateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
