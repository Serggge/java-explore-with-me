package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import java.time.LocalDateTime;
import static ru.practicum.explorewithme.util.Constants.DATE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventFullDto {

    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private long views;
    private long likes;
    private long dislikes;
}
