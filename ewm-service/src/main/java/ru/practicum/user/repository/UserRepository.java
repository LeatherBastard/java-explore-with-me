package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * " +
            "FROM users AS u " +
            "WHERE u.id IN (:ids) " +
            "ORDER BY u.id ASC " +
            "LIMIT :size OFFSET :from ", nativeQuery = true)
    List<User> findAllWithIds(@Param("ids") List<Integer> ids, @Param("from") int from, @Param("size") int size);
    @Query(value = "select * " +
            "FROM users AS u " +
            "ORDER BY u.id ASC " +
            "LIMIT :size OFFSET :from ", nativeQuery = true)
    List<User> findAllWithoutIds( @Param("from") int from, @Param("size") int size);
}
