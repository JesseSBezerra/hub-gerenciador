package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.SprintItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintItemRepository extends JpaRepository<SprintItem, Long> {
    List<SprintItem> findBySprintId(Long sprintId);
    List<SprintItem> findByTarefaId(Long tarefaId);
}
