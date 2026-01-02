package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.TarefaQuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaQuestionarioRepository extends JpaRepository<TarefaQuestionario, Long> {
    List<TarefaQuestionario> findByTarefaId(Long tarefaId);
}
