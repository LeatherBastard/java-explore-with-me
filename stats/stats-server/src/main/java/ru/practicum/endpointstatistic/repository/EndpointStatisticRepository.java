package ru.practicum.endpointstatistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.practicum.endpointstatistic.dto.EndpointStatisticResponseDto;
import ru.practicum.endpointstatistic.model.EndpointStatistic;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface EndpointStatisticRepository extends JpaRepository<EndpointStatistic, Integer> {
    @Query(value = "SELECT app , uri , COUNT( ip) as hits FROM endpoints_stat"
            + " WHERE uri IN (:uris) AND timestamp BETWEEN :start and :end GROUP BY uri,app ORDER BY hits DESC", nativeQuery = true)
    List<EndpointStatisticResponseDto> getStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);


    @Query(value = "SELECT app , uri , COUNT( ip) as hits FROM endpoints_stat"
            + " WHERE timestamp BETWEEN :start and :end GROUP BY uri,app ORDER BY hits DESC", nativeQuery = true)
    List<EndpointStatisticResponseDto> getStatsWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT DISTINCT app , uri , COUNT(DISTINCT ip) as hits FROM endpoints_stat"
            + " WHERE uri IN (:uris) AND timestamp BETWEEN :start and :end GROUP BY uri,app ORDER BY hits DESC", nativeQuery = true)
    List<EndpointStatisticResponseDto> getStatsUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT DISTINCT app , uri , COUNT(DISTINCT ip) as hits FROM endpoints_stat"
            + " WHERE timestamp BETWEEN :start and :end GROUP BY uri,app ORDER BY hits DESC", nativeQuery = true)
    List<EndpointStatisticResponseDto> getStatsUniqueWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
