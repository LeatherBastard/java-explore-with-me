package ru.practicum.statistic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.StatisticEventException;
import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;
import ru.practicum.statistic.mapper.StatisticMapper;
import ru.practicum.statistic.model.view.StatisticView;
import ru.practicum.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    public static final String ITEM_NOT_FOUND_MESSAGE = "Item with id %d not found";
    private static final String WRONG_OWNER_MESSAGE = "You are not an owner ot this item!";

    private static final String BOOKING_FOR_COMMENT_NOT_FOUND_EXCEPTION_MESSAGE = " You have not booked item %d to comment";

    private final StatisticMapper mapper;
    private final StatisticRepository statisticRepository;


    @Override
    public void addHit(StatisticRequestDto statisticRequestDto) {
        statisticRepository.save(mapper.mapToStatistic(statisticRequestDto));
    }


    @Override
    public List<StatisticResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (end.isBefore(start))
            throw new StatisticEventException(start, end);

        List<StatisticView> result;
        if (unique) {
            if (uris == null) {
                result = statisticRepository.getStatsUniqueWithoutUris(start, end);
            } else {
                result = statisticRepository.getStatsUnique(start, end, uris);
            }
        } else {
            if (uris == null) {
                result = statisticRepository.getStatsWithoutUris(start, end);
            } else {
                result = statisticRepository.getStats(start, end, uris);
            }

        }
        return result.stream().map(mapper::mapToStatisticResponseDto).collect(Collectors.toList());
    }
}