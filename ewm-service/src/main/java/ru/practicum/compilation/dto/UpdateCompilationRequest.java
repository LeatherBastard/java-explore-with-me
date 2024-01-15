package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private List<Integer> events;
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
}
