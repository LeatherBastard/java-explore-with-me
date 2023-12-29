package ru.practicum.endpointstatistic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.endpointstatistic.dto.EndpointStatisticRequestDto;
import ru.practicum.endpointstatistic.dto.EndpointStatisticResponseDto;
import ru.practicum.endpointstatistic.mapper.EndpointStatisticMapper;
import ru.practicum.endpointstatistic.repository.EndpointStatisticRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointStatisticServiceImpl implements EndpointStatisticService {
    public static final String ITEM_NOT_FOUND_MESSAGE = "Item with id %d not found";
    private static final String WRONG_OWNER_MESSAGE = "You are not an owner ot this item!";

    private static final String BOOKING_FOR_COMMENT_NOT_FOUND_EXCEPTION_MESSAGE = " You have not booked item %d to comment";

    private final EndpointStatisticMapper mapper;
    private final EndpointStatisticRepository endpointStatisticRepository;


    @Override
    public void addHit(EndpointStatisticRequestDto endpointStatisticRequestDto) {
        endpointStatisticRepository.save(mapper.mapToEndpointStatistic(endpointStatisticRequestDto));
    }


    @Override
    public List<EndpointStatisticResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<EndpointStatisticResponseDto> result;
        if (unique) {
            if (uris == null) {
                result = endpointStatisticRepository.getStatsUniqueWithoutUris(start, end);
            } else {
                result = endpointStatisticRepository.getStatsUnique(start, end, uris);
            }
        } else {
            if (uris == null) {
                result = endpointStatisticRepository.getStatsWithoutUris(start, end);
            } else {
                result = endpointStatisticRepository.getStats(start, end, uris);
            }

        }
        return result;
    }
}