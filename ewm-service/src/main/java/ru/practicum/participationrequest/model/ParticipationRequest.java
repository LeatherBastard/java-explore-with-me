package ru.practicum.participationrequest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "participation_requests", schema = "public")
public class ParticipationRequest {
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "status", nullable = false, length = 9)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
