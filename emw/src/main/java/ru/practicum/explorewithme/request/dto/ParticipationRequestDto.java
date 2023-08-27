package ru.practicum.explorewithme.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import static ru.practicum.explorewithme.util.Constants.DATE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParticipationRequestDto {

    private Long id;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private String status;
}
