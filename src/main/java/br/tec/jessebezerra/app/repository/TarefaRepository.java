package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.model.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByTarefaPaiId(Long tarefaPaiId);
    List<Tarefa> findByTipo(Tipo tipo);
}
