package ru.practicum.location.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.location.model.Location;
import ru.practicum.location.dto.LocationDto;

@Component
public class LocationMapper {
    public LocationDto mapToLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLatitude())
                .lon(location.getLongitude())
                .build();
    }

    public Location mapToLocation(LocationDto locationDto) {
        return Location.builder()
                .latitude(locationDto.getLat())
                .longitude(locationDto.getLon())
                .build();
    }
}
