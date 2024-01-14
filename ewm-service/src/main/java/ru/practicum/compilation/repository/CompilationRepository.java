package ru.practicum.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

}
