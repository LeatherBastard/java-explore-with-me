package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.location.model.Location;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    @Query("select new ru.practicum.location.model.Location(l.id,l.latitude,l.longitude) " +
            "FROM Location as l " +
            "WHERE l.latitude=:latitude AND l.longitude=:longitude")
    Optional<Location> findLocationByLatitudeAndLongitude(@Param("latitude") float latitude, @Param("longitude") float longitude);

}
