package ru.practicum.participationrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.participationrequest.model.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findByRequester_Id(int requesterId);

    List<ParticipationRequest> findByEvent_IdAndRequester_Id(int eventId, int requesterId);

    List<ParticipationRequest> findByEvent_Id(int eventId);

}
