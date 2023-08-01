package ru.practicum.explorewithme.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListStatisticDto {

    private List<StatisticDto> statistic;

    public ListStatisticDto() {
        this.statistic = new ArrayList<>();
    }

    public ListStatisticDto(List<StatisticDto> statistic) {
        this.statistic = statistic;
    }
}
