package ru.practicum.explorewithme.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDto {

    private List<Long> categoryIds;
    private List<String> categoryNames;
}
