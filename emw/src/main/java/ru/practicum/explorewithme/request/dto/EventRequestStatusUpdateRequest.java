package ru.practicum.explorewithme.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdateRequest {

    private Set<Long> requestIds;
    private String status;
}
