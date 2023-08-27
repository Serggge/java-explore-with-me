package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationDto {

    @NotNull
    @Min(-90)
    @Max(90)
    private Double lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private Double lon;
}
