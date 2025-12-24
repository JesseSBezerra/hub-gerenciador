package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.UsuarioTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioTarefaRepository extends JpaRepository<UsuarioTarefa, Long> {
    
    List<UsuarioTarefa> findByUsuarioId(Long usuarioId);
    
    List<UsuarioTarefa> findByTarefaId(Long tarefaId);
}
