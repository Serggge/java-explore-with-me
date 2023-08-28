package ru.practicum.explorewithme.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDto {

    private Set<Long> categoryIds;
    private Set<String> categoryNames;
}
