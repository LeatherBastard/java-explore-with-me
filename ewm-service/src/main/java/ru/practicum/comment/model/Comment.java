package ru.practicum.comment.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "text", nullable = false, length = 500)
    private String text;

}
