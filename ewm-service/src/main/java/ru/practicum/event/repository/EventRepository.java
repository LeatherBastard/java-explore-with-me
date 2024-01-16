package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "SELECT * FROM events WHERE " +
            "initiator_id=:userId " +
            "LIMIT :size OFFSET :from ", nativeQuery = true)
    List<Event> getEvents(@Param("userId") int userId,
                          @Param("from") int from,
                          @Param("size") int size);

    Optional<Event> findByInitiator_IdAndId(int userId, int eventId);

    List<Event> findByCategory_Id(int categoryId);
}
