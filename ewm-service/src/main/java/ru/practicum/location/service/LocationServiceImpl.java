package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public Location addLocation(LocationDto locationDto) {
        Location result;
        Optional<Location> location = locationRepository
                .findLocationByLatitudeAndLongitude(locationDto.getLatitude(), locationDto.getLongitude());
        if (location.isPresent()) {
            result = location.get();
        } else {
            result = locationRepository.save(locationMapper.mapToLocation(locationDto));
        }
        return result;
    }
}
