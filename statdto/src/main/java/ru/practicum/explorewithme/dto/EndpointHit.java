package ru.practicum.explorewithme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import static ru.practicum.explorewithme.dto.Constants.DATE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EndpointHit {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime timestamp;

}
