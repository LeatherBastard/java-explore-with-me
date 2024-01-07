package ru.practicum.category.repository;

import ru.practicum.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "select * " +
            "FROM categories AS c " +
            "ORDER BY c.id ASC " +
            "LIMIT :size OFFSET :from ", nativeQuery = true)
    List<Category> findAll(@Param("from") int from, @Param("size") int size);

}
