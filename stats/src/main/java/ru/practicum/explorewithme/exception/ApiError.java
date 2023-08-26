package ru.practicum.explorewithme.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
